package com.pkry.user.server;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Monika on 1/3/2016.
 */
public class Server implements Runnable {

    private List<Service> clients;
    private volatile boolean running;
    private int _lastID;
    private InetAddress inetAddress;
    private int port;
    private int backlog;
    private Handle handle;
    private SSLServerSocket serverSocket;

    private Server() {
        clients = new LinkedList<Service>();
        running = false;
        _lastID = -1;

        String path = null;
        try {
            path = new File(".").getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.setProperty("javax.net.ssl.keyStore", "mySrvKeystore");
        System.setProperty("javax.net.ssl.keyStorePassword", "123456");

    }

    public Server(int port, Handle handle) {
        this();
        this.port = port;
        this.handle = handle;
        setInetAddress();
        if (setServer()) {
            running = true;
            new Thread(this).start();
        }
        System.out.println("ServerApp created (" + getInetAdress() + ":" + port + ")");
    }

    private void setInetAddress() {
        try {
            inetAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            System.out.println("Exception in getting InetAdress");
            e.printStackTrace();
        }
    }

    public String getInetAdress() {
        return serverSocket.getInetAddress().toString();
    }

    public boolean isRunning() {
        return running;
    }

    public void run() {
        try {
            serverSocket.setSoTimeout(500);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        while (running) {
            try {
                addClientService(new Service((SSLSocket) serverSocket.accept(), this, handle));
            } catch (SocketTimeoutException e) {
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    synchronized void addClientService(Service clientService) throws IOException {
        clientService.init();
        clients.add(clientService);
        new Thread(clientService).start();
        System.out.println("ServerApp added new client service");
    }

    synchronized void removeClientService(Service clientService) {
        clients.remove(clientService);
        clientService.close();
        System.out.println("ServerApp removed new client service");
    }

    synchronized void send(String msg) {
        for (Service s : clients)
            s.send(msg);

        System.out.println("ServerApp sent to all services : " + msg);
    }

    synchronized int nextID() {
        return ++_lastID;
    }

    public void sendData(String data) {
        send(TProtocol.DATA + " " + data);
    }

    public void close() {
        send(TProtocol.STOP);
        while (clients.size() != 0) {
            try {
                Thread.sleep(50);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        running = false;
        try {
            serverSocket.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private boolean setServer() {
        try {
            SSLServerSocketFactory sslServerSocketfactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            serverSocket = (SSLServerSocket) sslServerSocketfactory.createServerSocket(port, backlog, inetAddress);
//            serverSocket = new ServerSocket(port, backlog, inetAddress);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            running = false;
            return false;
        }
    }
}

