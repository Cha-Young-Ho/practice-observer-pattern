import board.Board;
import client.Client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        ExecutorService es = Executors.newFixedThreadPool(200);
        Board board = Board.getInstance();
        Client client = new Client();
        for (int i = 0; i < 1000; i++) {
            es.submit(client);
        }

        es.shutdown();
        es.awaitTermination(200, TimeUnit.SECONDS);

        long endTime = System.currentTimeMillis();

        System.out.println(board.getViewCount());
        System.out.println("Running Time : " + (endTime - startTime));

    }
}