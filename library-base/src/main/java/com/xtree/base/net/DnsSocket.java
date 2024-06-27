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

/**
 * Created by KAKA on 2024/6/27.
 * Describe: DNS 解析 SOCKET
 */
public class DnsSocket {

    private static volatile DnsSocket instance;
    private volatile DatagramSocket socket;;
    private static final int TIME_OUT = 5000;;
    public static final String dns1 = "114.114.114.114";
    public static final String dns2 = "8.8.8.8";
    public static final int dnsPort = 53;;

    private DnsSocket() throws SocketException {
        socket = new DatagramSocket();
        socket.setSoTimeout(TIME_OUT);
    }

    public static DnsSocket getInstance() throws SocketException {
        if (instance == null) {
            synchronized (DnsSocket.class) {
                if (instance == null) {
                    instance = new DnsSocket();
                }
            }
        }
        return instance;
    }

    /**
     * 使用114.114.114.114 解析
     */
    public InetAddress resolveDNS1(String hostname) throws UnknownHostException {
        return resolveDNS(hostname, dns1, dnsPort);
    }

    /**
     * 使用8.8.8.8 解析
     */
    public InetAddress resolveDNS2(String hostname) throws UnknownHostException {
        return resolveDNS(hostname, dns2, dnsPort);
    }

    /**
     * 轮次使用dns1->dns2 解析hostname
     * @return 返回解析结果 若服务器解析失败则使用本地dns解析
     */
    public InetAddress resolveDNS(String hostname) throws UnknownHostException {
        try {
            byte[] dnsQuery = buildDNSQuery(hostname);
            DatagramPacket requestPacket = new DatagramPacket(dnsQuery, dnsQuery.length, InetAddress.getByName(dns1), dnsPort);
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
                return resolveDNS(hostname, dns2, dnsPort);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resolveDNS(hostname, dns2, dnsPort);
    }

    /**
     * 指定DNS服务器解析hostname
     * @param hostname 域名
     * @param dnsServer dns服务器地址
     * @param dnsPort 端口号
     * @return 返回解析结果 若服务器解析失败则使用本地dns解析
     */
    public InetAddress resolveDNS(String hostname, String dnsServer, int dnsPort) throws UnknownHostException {
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

    private byte[] buildDNSQuery(String hostname) {
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

    private String parseDNSResponse(byte[] response, int length) {
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

    private void skipQuestion(DataInputStream dataInputStream) throws IOException {
        while (dataInputStream.readByte() != 0) ;
        dataInputStream.readShort();
        dataInputStream.readShort();
    }

    private String readAnswer(DataInputStream dataInputStream) throws IOException {
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


    public static synchronized void closeInstance() {
        if (instance != null) {
            if (instance.socket != null) {
                instance.socket.close();
            }
            instance = null;
        }
    }
}
