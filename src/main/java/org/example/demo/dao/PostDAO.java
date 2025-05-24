package org.example.demo.dao;

import org.example.demo.model.Post;
import org.example.demo.service.DBConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostDAO {
    private static final Logger logger = LogManager.getLogger(PostDAO.class);

    public void createPost(Post post) throws SQLException {
        createPost(post, null);
    }

    public void createPost(Post post, Connection conn) throws SQLException {
        String sql = "INSERT INTO posts (title, content, user_id) VALUES (?, ?, ?)";
        Connection connection = (conn != null) ? conn : DBConnection.getConnection();
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, post.getTitle());
            ps.setString(2, post.getContent());
            ps.setInt(3, post.getUserId());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                post.setId(rs.getInt(1));
            }
            logger.debug("Post created: ID={}, title={}", post.getId(), post.getTitle());
        } finally {
            if (conn == null && connection != null) {
                connection.close();
            }
        }
    }

    public void updatePost(Post post) throws SQLException {
        updatePost(post, null);
    }

    public void updatePost(Post post, Connection conn) throws SQLException {
        String sql = "UPDATE posts SET title = ?, content = ? WHERE id = ?";
        Connection connection = (conn != null) ? conn : DBConnection.getConnection();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, post.getTitle());
            ps.setString(2, post.getContent());
            ps.setInt(3, post.getId());
            ps.executeUpdate();
            logger.debug("Post updated: ID={}", post.getId());
        } finally {
            if (conn == null && connection != null) {
                connection.close();
            }
        }
    }

    public void deletePost(int postId) throws SQLException {
        deletePost(postId, null);
    }

    public void deletePost(int postId, Connection conn) throws SQLException {
        String sql = "DELETE FROM posts WHERE id = ?";
        Connection connection = (conn != null) ? conn : DBConnection.getConnection();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, postId);
            ps.executeUpdate();
            logger.debug("Post deleted: ID={}", postId);
        } finally {
            if (conn == null && connection != null) {
                connection.close();
            }
        }
    }

    public List<Post> getPostsByUserId(int userId) throws SQLException {
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT * FROM posts WHERE user_id = ? ORDER BY created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Post post = parsePostFromDB(rs);
                posts.add(post);
            }
            logger.debug("Fetched {} posts for userId: {}", posts.size(), userId);
        }
        return posts;
    }

    public Post getPostById(int id) throws SQLException {
        String sql = "SELECT * FROM posts WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Post post = parsePostFromDB(rs);
                logger.debug("Fetched post ID: {}", id);
                return post;
            }
            logger.warn("Post not found for ID: {}", id);
            return null;
        }
    }

    private Post parsePostFromDB(ResultSet rs) throws SQLException {
        return new Post(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("content"),
                rs.getInt("user_id"),
                rs.getTimestamp("created_at")
        );
    }
}