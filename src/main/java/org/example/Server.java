package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Server implements AutoCloseable {
    private final ServerSocket acceptorSocket;
    private final Thread acceptorThread;

    public Server(int port) throws Exception {
        acceptorSocket = new ServerSocket(port);
        acceptorThread = new Thread(() -> checkAccepting(acceptorSocket));
        acceptorThread.start();
    }

    private static void checkAccepting(ServerSocket acceptorSocket) {
        try {
            accepting(acceptorSocket);
        } catch (Exception e) {
            System.err.println("Unable to accept.");
            e.printStackTrace();
        }
    }

    private static void accepting(ServerSocket acceptorSocket) throws IOException {
        while (!acceptorSocket.isClosed()) {
            accept(acceptorSocket);
        }
    }

    private static void accept(ServerSocket acceptorSocket) throws IOException {
        try (Socket accepted = acceptorSocket.accept()) {
            handle(accepted);
        }
    }

    private static void handle(Socket accepted) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(accepted.getInputStream()));
        boolean readingEnded = false;
        while (!readingEnded) {
            String line = reader.readLine();
            readingEnded = line.isEmpty();
        }
        OutputStream output = accepted.getOutputStream();
        output.write("HTTP/1.1 200 OK\r\nContent-Length: 4\r\n\r\nOk.\n".getBytes(StandardCharsets.UTF_8));
        output.flush();
    }

    @Override
    public void close() throws Exception {
        acceptorSocket.close();
    }
}
