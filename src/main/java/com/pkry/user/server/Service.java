package com.pkry.user.server;

import javax.net.ssl.SSLSocket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Service implements Runnable {

    private SSLSocket sslSocket;
    private boolean running;
    private Server server;
    private int id;
    private BufferedReader input;
    private PrintWriter output;
    private Handle handle;

    private Service() {
        running = false;
    }

    public Service(SSLSocket socket, Server server, Handle handle) {
        this();

        running = true;
        this.sslSocket = socket;
        this.server = server;
        this.handle = handle;
        System.out.println("Service " + id + " created");
    }

    public boolean isRunning() {
        return running;
    }

    public void init() throws IOException {
        input = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
        output = new PrintWriter(sslSocket.getOutputStream(), true);
    }

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

    public void send(String command) {
        output.println(command);
    }

    public void sendData(String data) {
        send(TProtocol.DATA + " " + data);
    }

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
            String msg = handle.handle(command.substring(command.indexOf(' ') + 1));
            if (msg != null)
                sendData(msg);
        } else if (command.equals(TProtocol.LOGOUT)) {
            send(TProtocol.LOGGEDOUT);
            running = false;
        } else if (command.equals(TProtocol.STOPPED)) {
            running = false;
        } else if (command.equals(TProtocol.NULLCOMMAND)) {
            sendData("siema");
            running = false;
        }
    }

    public void run() {
        while (running) {
            handleCommand(receive());
        }
        server.removeClientService(this);
    }
}
