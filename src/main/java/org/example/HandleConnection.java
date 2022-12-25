package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class HandleConnection {

    private final Socket connection;

    public HandleConnection(Socket connection) {
        this.connection = connection;
        try {
            consumeRequest();
            produceResponse();
        } catch (Exception e) {
            System.err.println("Error while handling connection.");
            e.printStackTrace();
        } finally {
            close();
        }
    }

    private void consumeRequest() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        boolean readingEnded = false;
        while (!readingEnded) {
            String line = reader.readLine();
            readingEnded = line.isEmpty();
        }
    }

    private void produceResponse() throws IOException {
        OutputStream output = connection.getOutputStream();
        output.write("HTTP/1.1 200 OK\r\nContent-Length: 4\r\n\r\nOk.\n".getBytes(StandardCharsets.UTF_8));
        output.flush();
    }

    private void close() {
        try {
            connection.close();
        } catch (IOException e) {
            System.err.println("Error while closing connection.");
            e.printStackTrace();
        }
    }
}
