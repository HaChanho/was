package org.example;

import java.io.BufferedReader;
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
        acceptorThread = new Thread(() -> {
            try {
                try (Socket accepted = acceptorSocket.accept()) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(accepted.getInputStream()));
                    while (true) {
                        String line = reader.readLine();
                        if (line.isEmpty()) {
                            break;
                        }
                    }
                    OutputStream output = accepted.getOutputStream();
                    output.write("HTTP/1.1 200 OK\r\nContent-Length: 4\r\n\r\nOk.\n".getBytes(StandardCharsets.UTF_8));
                    output.flush();
                }
            } catch (Exception e) {
                System.err.println("Unable to accept.");
                e.printStackTrace();
            }
        });
        acceptorThread.start();
    }

    @Override
    public void close() throws Exception {
        acceptorSocket.close();
    }
}
