package com.tora;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.concurrent.BlockingQueue;

public class UDPBroadcast implements Runnable {
    public volatile boolean terminated;
    private final static Logger logger = LoggerFactory.getLogger(UDPBroadcast.class);
    private final BlockingQueue<Connection> pendingConnections;

    private String port;

    UDPBroadcast(BlockingQueue<Connection> queue) {
        pendingConnections = queue;
    }

    public void setPort(int port) {
        this.port = String.valueOf(port);
    }

    public void terminate() {
        terminated = true;
    }

    public void sendPackage(byte[] buffer) {
        try (DatagramSocket socket = new DatagramSocket();){
            socket.setBroadcast(true);
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("10.4.1.255"), 8000);
            socket.send(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        //Keep a socket open to listen to all the UDP trafic that is destined for this port
        try (DatagramSocket socket = new DatagramSocket(8000, InetAddress.getByName("0.0.0.0"))) {
            socket.setBroadcast(true);

            while (!terminated) {
                logger.info("Ready to receive broadcast packets!");

                //Receive a packet
                byte[] recvBuf = new byte[1024];
                DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
                try {
                    socket.receive(packet);
                } catch (IOException e) {
                    logger.info("Couldn't receive packet");
                    continue;
                }

                // get actual length of data and trim the byte array accordingly
                int length = packet.getLength();
                byte[] data = Arrays.copyOfRange(packet.getData(), 0, length);
                packet.setData(data);

                //Packet received

                String sender = packet.getAddress().getHostAddress();

                // if received message is from myself, skip
                if (checkSelf(sender)) {
                   continue;
                }

                String message = new String(data);
                logger.info("Packet received from: {}; message: {}", sender, message);
                if ("who can connect?".equals(message)) {
                    sendPackage(port.getBytes());
                } else {
                    Socket userSocket = new Socket(sender, Integer.parseInt(message));
                    Connection connection = new Connection(userSocket);
                    pendingConnections.add(connection);
                }
            }
        } catch (IOException e) {
            logger.info("Error: {}", e.getMessage());
        }
    }

    private static boolean checkSelf(String sender) throws SocketException {
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            Enumeration<InetAddress> address =  interfaces.nextElement().getInetAddresses();
            while (address.hasMoreElements()) {
                if (address.nextElement().getHostAddress().equals(sender)) {
                    return true;
                }
            }
        }
        return false;
    }
}
