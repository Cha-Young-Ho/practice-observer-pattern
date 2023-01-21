package board;

import lock.SpinLock;

import java.util.UUID;

public class Board {

    private static Board board;
    private int viewCount;
    private String title;
    private String content;
    private String id;

    private SpinLock spinLock;

    private Board(int viewCount, String title, String content, String id){
        this.viewCount = viewCount;
        this.title = title;
        this.content = content;
        this.id = id;
        this.spinLock = new SpinLock();
    }

    public static Board getInstance(){
        if(board == null){
            board = new Board(0, "Title", "Content", UUID.randomUUID().toString());
        }

        return board;
    }

    public void updateViewCount() {

        spinLock.acquire();

        // -- 해당 로직이 10ms 걸린다는 조건 --
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.viewCount++;

        spinLock.release();
    }

    // --- Getter ---
    public Board getBoard() {
        return board;
    }

    public int getViewCount() {
        return viewCount;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getId() {
        return id;
    }
}
