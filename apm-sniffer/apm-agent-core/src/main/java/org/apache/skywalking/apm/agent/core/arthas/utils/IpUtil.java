/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.skywalking.apm.agent.core.arthas.utils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class IpUtil {

    private static volatile InetAddress cachedAddress;

    public static InetAddress getLocalHostExactAddress() {
        InetAddress cached = cachedAddress;
        if (cached != null) {
            return cached;
        }
        synchronized (IpUtil.class) {
            if (cachedAddress != null) {
                return cachedAddress;
            }
            try {
                InetAddress candidateAddress = null;
                Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
                while (networkInterfaces.hasMoreElements()) {
                    NetworkInterface iface = networkInterfaces.nextElement();
                    if (isInvalidInterface(iface)) {
                        continue;
                    }
                    for (Enumeration<InetAddress> inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
                        InetAddress inetAddr = inetAddrs.nextElement();
                        if (inetAddr instanceof Inet4Address && !inetAddr.isLoopbackAddress()) {
                            if (inetAddr.isSiteLocalAddress()) {
                                // 优先返回内网IPv4
                                cachedAddress = inetAddr;
                                return inetAddr;
                            }
                            if (candidateAddress == null) {
                                candidateAddress = inetAddr;
                            }
                        }
                    }
                }
                // 降级获取其余网卡的IPv4，兜底用hostname
                InetAddress result = candidateAddress != null ? candidateAddress : InetAddress.getLocalHost();
                cachedAddress = result;
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * 排除无效或虚拟网卡
     */
    private static boolean isInvalidInterface(NetworkInterface iface) throws Exception {
        if (!iface.isUp() || iface.isLoopback() || iface.isVirtual() || iface.isPointToPoint()) {
            return true;
        }
        String name = iface.getName().toLowerCase();
        return name.startsWith("utun") || name.startsWith("tun") || name.startsWith("tap")
                || name.startsWith("bridge") || name.startsWith("vmnet") || name.startsWith("vmenet")
                || name.startsWith("docker") || name.startsWith("veth") || name.startsWith("awdl")
                || name.startsWith("llw") || name.startsWith("anpi");
    }

}
