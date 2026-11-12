package com.back.domain.wiseSaying.entity;

public class WiseSaying {
    private final int id;
    private String content;
    private String author;

    public WiseSaying(int id, String content, String author) {
        this.id = id;
        this.content = content;
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public void modify(String content, String author) {
        this.content = content;
        this.author = author;
    }
}
