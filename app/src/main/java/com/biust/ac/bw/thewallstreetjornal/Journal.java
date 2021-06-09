package com.biust.ac.bw.thewallstreetjornal;

public class Journal {
    String date;
    String title;
    String content;
    String Id;

    public Journal(String date, String title, String content, String id) {
        this.date = date;
        this.title = title;
        this.content = content;
        Id = id;
    }

    public Journal() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
