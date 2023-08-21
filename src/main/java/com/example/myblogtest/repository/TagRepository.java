package com.example.myblogtest.repository;

import com.example.myblogtest.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Page<Tag> findByPostsId(Long postId, Pageable pageable);
    Optional<Tag> findByIdAndPostsId(Long id, Long postId);
//    Page<Tag> findByPostId(Long postId, Pageable pageable);
}
