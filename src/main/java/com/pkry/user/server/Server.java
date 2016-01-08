package com.pkry.user.server;

import com.pkry.user.HandleClient;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Server class responsible for adding new clients and running threads.
 */
@Singleton
public class Server implements Runnable {


    /**
     * List of clients.
     */
    private List<Service> clients;

    /**
     * Says whether or not the server is running
     */
    private volatile boolean running;

    /**
     * last ID info
     */
    private int _lastID;

    /**
     * Server Internet address.
     */
    private InetAddress inetAddress;

    /**
     * port to run the server at
     */
    private int port;

    /**
     * Backlog information
     */
    private int backlog;

    /**
     * A server network socket
     */
    private SSLServerSocket serverSocket;

    /**
     * Injected object of handleClass
     */
    @Inject
    HandleClient handleClient;
    Handle handle;


    /**
     * Creates a server and sets the keystore - repository of security certificates.
     */
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
//        this.handle = handle;
    }

    @PostConstruct
    public void init(){
        handle = handleClient;
    }

    public Handle getHandle() {
        return handle;
    }

    public void setHandle(Handle handle) {
        this.handle = handle;
    }

    public int getPort() {
        return port;
    }

    /**
     * Sets port on which the server is meant to run
     * @param port port for the server
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Starts the server
     */
    public void start(){
        setInetAddress();
        if (setServer()) {
            running = true;
            new Thread(this).start();
        }
        System.out.println("ServerApp created (" + getInetAdress() + ":" + port + ")");
    }

    /**
     * Sets Internet address from user network.
     */
    private void setInetAddress() {
//        try {
////            inetAddress = InetAddress.getLocalHost();
//        } catch (UnknownHostException e) {
//            System.out.println("Exception in getting InetAdress");
//            e.printStackTrace();
//        }
        inetAddress = InetAddress.getLoopbackAddress();
    }

    /**
     * Gets Internet address
     * @return Internet address.
     */
    public String getInetAdress() {
        return serverSocket.getInetAddress().toString();
    }

    /**
     * determinate wheather of not the server is running
     * @return <i>true</i> when server is running
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * runs the server
     */
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
    /**
     * Adds new client to the server
     * @param clientService service for a client
     * @throws IOException service cannot be started
     */
    synchronized void addClientService(Service clientService) throws IOException {
        clientService.init();
        clients.add(clientService);
        new Thread(clientService).start();
        System.out.println("ServerApp added new client service");
    }

    /**
     * Removes client from the server
     * @param clientService client to remove from the server
     */
    synchronized void removeClientService(Service clientService) {
        clients.remove(clientService);
        clientService.close();
        System.out.println("ServerApp removed new client service");
    }

    /**
     * Sends a message to clients
     * @param msg message to send
     */
    synchronized void send(String msg) {
        for (Service s : clients)
            s.send(msg);

        System.out.println("ServerApp sent to all services : " + msg);
    }

    /**
     * Gets next client ID
     * @return ID of next client
     */
    synchronized int nextID() {
        return ++_lastID;
    }

    /**
     * Sends data to client
     * @param data data to send
     */
    public void sendData(String data) {
        send(TProtocol.DATA + " " + data);
    }

    /**
     * Closes the server
     */
    public void close() {
        send(TProtocol.STOP);
        int cnt = 0;
        while (clients.size() != 0 || cnt < 10) {
            try {
                Thread.sleep(50);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cnt++;
        }
        running = false;
        try {
            serverSocket.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Sets a server
     * @return <i>true</i> if the server was set
     */
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

