package org.example.demo.dao;

import org.example.demo.model.Post;
import org.example.demo.model.User;
import org.example.demo.service.DBConnection;

import java.sql.*;

public class PostDAO {
    public void createPost(Post post) throws SQLException {
        String sql = "INSERT INTO posts (title, content, user_id) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, post.getTitle());
            ps.setString(2, post.getContent());
            ps.setInt(3, post.getUserId());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                post.setId(rs.getInt(1));
            }
        }
    }
    private Post getPostByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM posts WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return parsePostFromDB(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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
