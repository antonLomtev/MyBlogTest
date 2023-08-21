package com.example.myblogtest.repository;

import com.example.myblogtest.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByTagsId(Long tagId, Pageable pageable);
    Optional<Post> findByIdAndTagsId(Long id, Long tagId);
    Page<Post> findByUserProfileId(Long userProfileId, Pageable pageable);
    void deleteByUserProfileId(Long userProfileId);
}
