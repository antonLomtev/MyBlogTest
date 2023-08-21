package com.example.myblogtest.repository;

import com.example.myblogtest.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByPostId(Long postId, Pageable pageable);
    Optional<Comment> findByIdAndPostId(Long id, Long postId);
    Page<Comment> findByUserId(Long userId, Pageable pageable);
    Page<Comment> findByPostIdAndUserId(Long postId, Long userId, Pageable pageable);
}
