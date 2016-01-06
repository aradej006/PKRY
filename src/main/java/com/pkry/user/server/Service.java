package com.pkry.user.server;

import javax.net.ssl.SSLSocket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

/**
 * Service for a client. This class is responsible for sending to and receiving messages from the client.
 */
public class Service implements Runnable {
    /**
     * Network client socket
     */
    private SSLSocket sslSocket;
    /**
     * Says whether or not the server is running
     */
    private boolean running;

    /**
     * Interpretation of server in this server application.
     */
    private Server server;

    /**
     * Service identifier.
     */
    private int id;

    /**
     * Input stream for receiving data.
     */
    private BufferedReader input;

    /**
     * Output stream for receiving data.
     */
    private PrintWriter output;

    /**
     * Handle object
     */
    private Handle handle;

    /**
     * Creates new service.
     */
    private Service() {
        running = false;
    }

    /**
     * Creates new service.
     * @param socket  Client socket on server.
     * @param server server
     * @param handle handle object
     */
    public Service(SSLSocket socket, Server server, Handle handle) {
        this();

        running = true;
        this.sslSocket = socket;
        this.server = server;
        this.handle = handle;
        System.out.println("Service " + id + " created");
    }

    /**
     * Determinate whether or not the server is running.
     * @return <i>true</i> when server is running
     */
    public boolean isRunning() {
        return running;
    }


    /**
     * Creates input and output stream.
     * @throws IOException When input, output stream cannot be created.
     */
    public void init() throws IOException {
        input = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
        output = new PrintWriter(sslSocket.getOutputStream(), true);
    }

    /**
     * Closes all streams and deletes network socket.
     */
    public void close() {
        try {
            output.close();
            input.close();
            sslSocket.close();
        } catch (IOException e) {
            System.out.println("Error closing client (" + id + ").");
        } finally {
            output = null;
            input = null;
            sslSocket = null;
        }

    }

    /**
     * Sends <i>command</i>
     * @param command Information to send.
     */
    public void send(String command) {
        output.println(command);
    }

    /**
     * Sends data to client
     * @param data data to send
     */
    public void sendData(String data) {
        send(TProtocol.DATA + " " + data);
    }



    /**
     * Method used in order to receive messages from the client.
     * @return received message.
     */
    public String receive() {
        String message;
        try {
            message = input.readLine();
        } catch (IOException e) {
            System.out.println("Error reading client (" + id + ").");
            message = TProtocol.NULLCOMMAND;
        }
        return message;
    }

    /**
     * Function is responsible for handling messages received from the client.
     * @param request request to handle
     */
    private void handleCommand(String request) {
        StringTokenizer st = new StringTokenizer(request);
        String command = st.nextToken();

        if (command.equals(TProtocol.LOGIN)) {
            id = server.nextID();
            send(TProtocol.LOGGEDIN + " " + id);
        } else if (command.equals(TProtocol.LOGIN)) {
            String data = command.substring(command.indexOf(" ") + 1);
            String msg = handle.handle(data);
            if (msg != null) sendData(msg);
        } else if (command.equals(TProtocol.DATA)) {
            String msg = handle.handle(request.substring(request.indexOf(' ') + 1));
            if (msg != null)
                sendData(msg);
        } else if (command.equals(TProtocol.LOGOUT)) {
            send(TProtocol.LOGGEDOUT);
            running = false;
        } else if (command.equals(TProtocol.STOPPED)) {
            running = false;
        } else if (command.equals(TProtocol.NULLCOMMAND)) {
            running = false;
        }
    }



    /**
     * Method which services client.
     */
    public void run() {
        while (running) {
            handleCommand(receive());
        }
        server.removeClientService(this);
    }
}
