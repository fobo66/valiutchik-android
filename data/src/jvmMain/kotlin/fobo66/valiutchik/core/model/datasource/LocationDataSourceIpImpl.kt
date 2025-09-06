/*
 *    Copyright 2025 Andrey Mukamolov
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package fobo66.valiutchik.core.model.datasource

import fobo66.valiutchik.core.entities.Location
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.UnknownHostException
import java.util.Enumeration

class LocationDataSourceIpImpl : LocationDataSource {
    override suspend fun resolveLocation(): Location {
        TODO("Not yet implemented")
    }

    // https://stackoverflow.com/a/20418809/4606884
    private fun resolveIp(): InetAddress {
        try {
            var candidateAddress: InetAddress? = null
            // Iterate all NICs (network interface cards)...
            val ifaces: Enumeration<NetworkInterface> = NetworkInterface.getNetworkInterfaces()
            while (ifaces.hasMoreElements()) {
                val iface = ifaces.nextElement() as NetworkInterface
                // Iterate all IP addresses assigned to each card...
                val inetAddrs: Enumeration<InetAddress> = iface.getInetAddresses()
                while (inetAddrs.hasMoreElements()) {
                    val inetAddr = inetAddrs.nextElement() as InetAddress
                    if (!inetAddr.isLoopbackAddress()) {
                        if (inetAddr.isSiteLocalAddress()) {
                            // Found non-loopback site-local address. Return it immediately...
                            return inetAddr
                        } else if (candidateAddress == null) {
                            // Found non-loopback address, but not necessarily site-local.
                            // Store it as a candidate to be returned if site-local address is not subsequently found...
                            candidateAddress = inetAddr
                            // Note that we don't repeatedly assign non-loopback non-site-local addresses as candidates,
                            // only the first. For subsequent iterations, candidate will be non-null.
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                // We did not find a site-local address, but we found some other non-loopback address.
                // Server might have a non-site-local address assigned to its NIC (or it might be running
                // IPv6 which deprecates the "site-local" concept).
                // Return this non-loopback candidate address...
                return candidateAddress
            }
            // At this point, we did not find a non-loopback address.
            // Fall back to returning whatever InetAddress.getLocalHost() returns...
            val jdkSuppliedAddress = InetAddress.getLocalHost()
            if (jdkSuppliedAddress == null) {
                throw UnknownHostException(
                    "The JDK InetAddress.getLocalHost() method unexpectedly returned null."
                )
            }
            return jdkSuppliedAddress
        } catch (e: Exception) {
            val unknownHostException: UnknownHostException =
                UnknownHostException("Failed to determine LAN address: " + e)
            unknownHostException.initCause(e)
            throw unknownHostException
        }
    }
}
