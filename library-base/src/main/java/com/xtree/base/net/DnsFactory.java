package com.xtree.base.net;

import com.xtree.base.utils.CfLog;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Dns;

/**
 * Created by KAKA on 2024/6/18.
 * Describe: DNS配置工厂
 */
public class DnsFactory {
    public static Dns getDns() {
        //可以根据条件配置DNS
        return new EcDns();
    }

    //PRE环境DNS
    private static class PreDns implements Dns {
        @Override
        public List<InetAddress> lookup(String hostname) throws UnknownHostException {
            return SYSTEM.lookup(hostname);
        }
    }

    //生产环境DNS
    private static class EcDns implements Dns {
        private List<InetAddress> dnsServers = null;

        @Override
        public List<InetAddress> lookup(String hostname) throws UnknownHostException {
            InetAddress address = InetAddress.getByName(hostname);

            if (address.getHostAddress().equals(address.getHostName())) {
                CfLog.i(hostname + " is an IP address");
                return SYSTEM.lookup(hostname);
            } else {
                CfLog.i(hostname + " is an domain name");
                dnsServers = new ArrayList<>();
                dnsServers.add(InetAddress.getByAddress(hostname, new byte[]{114, 114, 114, 114}));
                dnsServers.add(InetAddress.getByAddress(hostname, new byte[]{8, 8, 8, 8}));
                return dnsServers;
            }
        }
    }
}
