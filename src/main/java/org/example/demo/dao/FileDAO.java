package org.example.demo.dao;

import org.example.demo.model.File;
import org.example.demo.service.DBConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FileDAO {
    private static final Logger logger = LogManager.getLogger(FileDAO.class);

    public void createFile(File file) throws SQLException {
        createFile(file, null);
    }

    public void createFile(File file, Connection conn) throws SQLException {
        String sql = "INSERT INTO files (file_name, file_data, content_type, post_id) VALUES (?, ?, ?, ?)";
        Connection connection = (conn != null) ? conn : DBConnection.getConnection();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, file.getFileName());
            ps.setBytes(2, file.getFileData());
            ps.setString(3, file.getContentType());
            ps.setInt(4, file.getPostId());
            ps.executeUpdate();
            logger.debug("File created: name={}", file.getFileName());
        } finally {
            if (conn == null && connection != null) {
                connection.close();
            }
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
                        rs.getBytes("file_data"),
                        rs.getString("content_type"),
                        rs.getInt("post_id"),
                        rs.getTimestamp("uploaded_at")
                ));
            }
            logger.debug("Fetched {} files for postId: {}", files.size(), postId);
        }
        return files;
    }

    public File getFileById(int id) throws SQLException {
        String sql = "SELECT * FROM files WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                File file = new File(
                        rs.getInt("id"),
                        rs.getString("file_name"),
                        rs.getBytes("file_data"),
                        rs.getString("content_type"),
                        rs.getInt("post_id"),
                        rs.getTimestamp("uploaded_at")
                );
                logger.debug("Fetched file ID: {}", id);
                return file;
            }
        }
        logger.warn("File not found for ID: {}", id);
        return null;
    }

    public void deleteFilesByPostId(int postId) throws SQLException {
        deleteFilesByPostId(postId, null);
    }

    public void deleteFilesByPostId(int postId, Connection conn) throws SQLException {
        String sql = "DELETE FROM files WHERE post_id = ?";
        Connection connection = (conn != null) ? conn : DBConnection.getConnection();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, postId);
            ps.executeUpdate();
            logger.debug("Deleted files for postId: {}", postId);
        } finally {
            if (conn == null && connection != null) {
                connection.close();
            }
        }
    }
}