package org.example;

import java.io.IOException;
import java.net.ServerSocket;

public class AcceptConnections implements AutoCloseable {

    private final ServerSocket socket;
    private final Thread thread;

    public AcceptConnections(int port) throws IOException {
        socket = new ServerSocket(port);
        thread = new Thread(this::checkAccepting);
        thread.start();
    }

    @Override
    public void close() throws Exception {
        socket.close();
    }

    private void checkAccepting() {
        try {
            accepting();
        } catch (Exception e) {
            System.err.println("Unable to accept.");
            e.printStackTrace();
        }
    }

    private void accepting() throws IOException {
        while (!socket.isClosed()) {
            new HandleConnection(socket.accept());
        }
    }
}
