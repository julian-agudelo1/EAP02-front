package co.com.devnet.screenplay.models;

public class CommentModel {
    private final String content;
    private final String author;

    public CommentModel(String content, String author) {
        this.content = content;
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }
}
