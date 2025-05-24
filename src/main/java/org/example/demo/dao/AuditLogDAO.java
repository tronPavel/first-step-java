package org.example.demo.dao;

import org.example.demo.service.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AuditLogDAO {
    public void logAction(int userId, String action, String details) throws SQLException {
        logAction(userId, action, details, null);
    }

    public void logAction(int userId, String action, String details, Connection conn) throws SQLException {
        String sql = "INSERT INTO audit_log (user_id, action, details, timestamp) VALUES (?, ?, ?, NOW())";
        Connection connection = (conn != null) ? conn : DBConnection.getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (userId > 0) {
                stmt.setInt(1, userId);
            } else {
                stmt.setNull(1, java.sql.Types.INTEGER); // Если userId недействителен
            }
            stmt.setString(2, action);
            stmt.setString(3, details);
            stmt.executeUpdate();
        }
    }
}