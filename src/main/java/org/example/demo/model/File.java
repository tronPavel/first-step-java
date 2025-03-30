package org.example.demo.model;

import java.sql.Timestamp;

public class File {
    private int id;
    private String fileName;
    private byte[] fileData; // Для хранения в базе (или String filePath для файловой системы)
    private String contentType;
    private int postId;
    private Timestamp uploadedAt;

    public File() {}
    public File(int id, String fileName, byte[] fileData, String contentType, int postId, Timestamp uploadedAt) {
        this.id = id;
        this.fileName = fileName;
        this.fileData = fileData;
        this.contentType = contentType;
        this.postId = postId;
        this.uploadedAt = uploadedAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public byte[] getFileData() { return fileData; }
    public void setFileData(byte[] fileData) { this.fileData = fileData; }
    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }
    public int getPostId() { return postId; }
    public void setPostId(int postId) { this.postId = postId; }
    public Timestamp getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(Timestamp uploadedAt) { this.uploadedAt = uploadedAt; }
}