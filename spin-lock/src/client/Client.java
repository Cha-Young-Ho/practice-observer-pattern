package client;

import board.Board;
import server.Server;

public class Client implements Runnable{

    @Override
    public void run() {
        Server server = Server.getInstance();
        server.getBoard();
    }
}
