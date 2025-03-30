package org.example.demo.model;

import java.sql.Timestamp;
import java.util.List;

public class Post {
    private int id;
    private String title;
    private String content;
    private int userId;
    private Timestamp createdAt;
    private List<File> files; // Список файлов, привязанных к посту

    public Post() {}
    public Post(int id, String title, String content, int userId, Timestamp createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.userId = userId;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    public List<File> getFiles() { return files; }
    public void setFiles(List<File> files) { this.files = files; }
}