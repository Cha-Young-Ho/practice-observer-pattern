package server;

import board.Board;

public class Server {

    private static Board board;
    private static Server server;

    private Server(){
        board = Board.getInstance();
    }

    public static Server getInstance(){
        if(server == null){
            server = new Server();
        }
        return server;
    }

    public Board getBoard(){
        // -- 조회수 증가 --
        board.updateViewCount();

        // 조회된 Board Return
        return board;
    }

}
