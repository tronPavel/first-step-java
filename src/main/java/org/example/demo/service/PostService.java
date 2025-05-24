package org.example.demo.service;

import org.example.demo.dao.FileDAO;
import org.example.demo.dao.PostDAO;
import org.example.demo.model.File;
import org.example.demo.model.Post;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class PostService {
    private static final Logger logger = LogManager.getLogger(PostService.class);
    private PostDAO postDAO = new PostDAO();
    private FileDAO fileDAO = new FileDAO();

    public Post getPostById(int id) throws SQLException {
        Post post = postDAO.getPostById(id);
        if (post != null) {
            post.setFiles(fileDAO.getFilesByPostId(id));
            logger.debug("Fetched post with files: ID={}", id);
        } else {
            logger.warn("Post not found: ID={}", id);
        }
        return post;
    }

    public List<Post> getPostsByUserId(int userId) throws SQLException {
        List<Post> posts = postDAO.getPostsByUserId(userId);
        logger.debug("Fetched {} posts for userId: {}", posts.size(), userId);
        return posts;
    }

    public void createPost(Post post, List<File> files) throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            createPost(post, files, conn);
        }
    }

    public void createPost(Post post, List<File> files, Connection conn) throws SQLException {
        conn.setAutoCommit(false);
        try {
            postDAO.createPost(post, conn);
            for (File file : files) {
                file.setPostId(post.getId());
                fileDAO.createFile(file, conn);
            }
            conn.commit();
            logger.info("Post created with ID: {}, files: {}", post.getId(), files.size());
        } catch (SQLException e) {
            conn.rollback();
            logger.error("Error creating post, rolled back", e);
            throw e;
        }
    }

    public void updatePost(Post post) throws SQLException {
        postDAO.updatePost(post);
        logger.debug("Post updated: ID={}", post.getId());
    }

    public void deletePost(int postId) throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                fileDAO.deleteFilesByPostId(postId, conn);
                postDAO.deletePost(postId, conn);
                conn.commit();
                logger.info("Post deleted with ID: {}", postId);
            } catch (SQLException e) {
                conn.rollback();
                logger.error("Error deleting post ID: {}, rolled back", postId, e);
                throw e;
            }
        }
    }
}