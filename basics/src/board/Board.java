package board;

import java.util.UUID;

public class Board {

    private static Board board;
    private int viewCount;
    private String title;
    private String content;
    private String id;

    private Board(int viewCount, String title, String content, String id){
        this.viewCount = viewCount;
        this.title = title;
        this.content = content;
        this.id = id;
    }

    public static Board getInstance(){
        if(board == null){
            board = new Board(0, "Title", "Content", UUID.randomUUID().toString());
        }

        return board;
    }

    public void updateViewCount(){
        this.viewCount++;
    }

    // --- Getter ---
    public Board getBoard() {
        return board;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
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
