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

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class IpUtil {

    public static InetAddress getLocalHostExactAddress() {
        try {
//            InetAddress candidateAddress = null;
//
//            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
//            while (networkInterfaces.hasMoreElements()) {
//                NetworkInterface iface = networkInterfaces.nextElement();
//                for (Enumeration<InetAddress> inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
//                    InetAddress inetAddr = inetAddrs.nextElement();
//                    if (!inetAddr.isLoopbackAddress()) {
//                        if (inetAddr.isSiteLocalAddress()) {
//                            return inetAddr;
//                        }
//                        if (candidateAddress == null) {
//                            candidateAddress = inetAddr;
//                        }
//                    }
//                }
//            }
//            return candidateAddress == null ? InetAddress.getLocalHost() : candidateAddress;
            return InetAddress.getLocalHost();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
