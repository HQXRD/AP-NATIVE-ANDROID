package com.xtree.base.net;

import com.xtree.base.utils.CfLog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
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
        private static final String dns1 = "114.114.114.114";
        private static final String dns2 = "8.8.8.8";
        private static final int dnsPort = 53;;

        @Override
        public List<InetAddress> lookup(String hostname) throws UnknownHostException {
            InetAddress address = InetAddress.getByName(hostname);

            if (address.getHostAddress().equals(address.getHostName())) {
                return SYSTEM.lookup(hostname);
            } else {
                CfLog.i(hostname + " is an domain name");
                try {
                    DatagramSocket socket = new DatagramSocket();
                    dnsServers = new ArrayList<>();
                    dnsServers.add(resolveDNS(socket,hostname, dns1, dnsPort));
                    dnsServers.add(resolveDNS(socket, hostname, dns2, dnsPort));
                    socket.close();
                    return dnsServers;
                } catch (SocketException e) {
                    e.printStackTrace();
                    return Collections.singletonList(InetAddress.getByName(hostname));
                }
            }
        }
    }

    public static InetAddress resolveDNS(DatagramSocket socket, String hostname, String dnsServer, int dnsPort) throws UnknownHostException {
        try {
            byte[] dnsQuery = buildDNSQuery(hostname);
            DatagramPacket requestPacket = new DatagramPacket(dnsQuery, dnsQuery.length, InetAddress.getByName(dnsServer), dnsPort);
            socket.send(requestPacket);
            byte[] response = new byte[1024];
            DatagramPacket responsePacket = new DatagramPacket(response, response.length);
            socket.receive(responsePacket);


            String ip = parseDNSResponse(responsePacket.getData(), responsePacket.getLength());

            if (ip != null) {
                CfLog.i("DNS获取成功 " + hostname + ": " + ip);
                return InetAddress.getByName(ip);
            } else {
                CfLog.e("dns解析失败 " + hostname);
                return InetAddress.getByName(hostname);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return InetAddress.getByName(hostname);
    }

    private static byte[] buildDNSQuery(String hostname) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        try {
            int queryId = (int) (Math.random() * 65536);
            dataOutputStream.writeShort(queryId);
            dataOutputStream.writeShort(0x0100);
            dataOutputStream.writeShort(1);
            dataOutputStream.writeShort(0);
            dataOutputStream.writeShort(0);
            dataOutputStream.writeShort(0);
            String[] parts = hostname.split("\\.");
            for (String part : parts) {
                byte[] partBytes = part.getBytes("UTF-8");
                dataOutputStream.writeByte(partBytes.length);
                dataOutputStream.write(partBytes);
            }
            dataOutputStream.writeByte(0);

            dataOutputStream.writeShort(1);
            dataOutputStream.writeShort(1);

            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return byteArrayOutputStream.toByteArray();
    }

    private static String parseDNSResponse(byte[] response, int length) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(response,
                0, length);
        DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);

        try {
            dataInputStream.readShort();
            int flags = dataInputStream.readShort();
            dataInputStream.readShort();
            int answerRRs = dataInputStream.readShort();
            dataInputStream.readShort();
            dataInputStream.readShort();

            if ((flags & 0x8000) != 0x8000 || answerRRs == 0) {
                return null;
            }

            skipQuestion(dataInputStream);

            String ipAddress = null;
            for (int i = 0; i < answerRRs; i++) {
                if ((ipAddress = readAnswer(dataInputStream)) != null) {
                    break;
                }
            }

            return ipAddress;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static void skipQuestion(DataInputStream dataInputStream) throws IOException {
        while (dataInputStream.readByte() != 0) ;
        dataInputStream.readShort();
        dataInputStream.readShort();
    }

    private static String readAnswer(DataInputStream dataInputStream) throws IOException {
        if (dataInputStream.readByte() != (byte) 0xc0) {
            return null;
        }
        dataInputStream.readByte();
        int type = dataInputStream.readShort();
        dataInputStream.readShort();
        dataInputStream.readInt();
        int length = dataInputStream.readShort();
        if (type == 1 && length == 4) {
            byte[] ipBytes = new byte[length];
            dataInputStream.read(ipBytes);
            return InetAddress.getByAddress(ipBytes).getHostAddress();
        }
        dataInputStream.skipBytes(length);
        return null;
    }

    //这个不行
//    private static String readAnswer(DataInputStream dataInputStream) throws IOException {
//        if (dataInputStream.readByte() != (byte) 0xc0) {
//            return null;
//        }
//        dataInputStream.readByte();
//        int type = dataInputStream.readShort();
//        dataInputStream.readShort();
//        int length = dataInputStream.readShort();
//        if (type == 1 && length == 4) {
//            byte[] ipBytes = new byte[length];
//            dataInputStream.read(ipBytes);
//            return InetAddress.getByAddress(ipBytes).getHostAddress();
//        }
//        dataInputStream.skipBytes(length);
//        return null;
//    }
}
