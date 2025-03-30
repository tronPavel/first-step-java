package org.example.demo.dao;

import org.example.demo.model.File;
import org.example.demo.service.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FileDAO {
    public void createFile(File file) throws SQLException {
        String sql = "INSERT INTO files (file_name, file_data, content_type, post_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, file.getFileName());
            ps.setBytes(2, file.getFileData()); // Или ps.setString(2, file.getFilePath()) для пути
            ps.setString(3, file.getContentType());
            ps.setInt(4, file.getPostId());
            ps.executeUpdate();
        }
    }

    public List<File> getFilesByPostId(int postId) throws SQLException {
        String sql = "SELECT * FROM files WHERE post_id = ?";
        List<File> files = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, postId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                files.add(new File(
                        rs.getInt("id"),
                        rs.getString("file_name"),
                        rs.getBytes("file_data"), // Или rs.getString("file_path")
                        rs.getString("content_type"),
                        rs.getInt("post_id"),
                        rs.getTimestamp("uploaded_at")
                ));
            }
        }
        return files;
    }
}