package client;

import server.Server;

public class Client implements Runnable{

    @Override
    public void run() {
        Server server = Server.getInstance();

        try {
            server.getBoard();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
