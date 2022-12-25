package org.example;

public class Server implements AutoCloseable {

    private final AcceptConnections accept;

    public Server(int port) throws Exception {
        accept = new AcceptConnections(port);
    }

    @Override
    public void close() throws Exception {
        accept.close();
    }
}
