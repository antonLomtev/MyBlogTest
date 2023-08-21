package com.example.myblogtest.controller;

import com.example.myblogtest.entity.Post;
import com.example.myblogtest.entity.Tag;
import com.example.myblogtest.entity.User;
import com.example.myblogtest.entity.UserProfile;
import com.example.myblogtest.repository.PostRepository;
import com.example.myblogtest.repository.TagRepository;
import com.example.myblogtest.repository.UserProfileRepository;
import com.example.myblogtest.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.NoSuchElementException;
import java.util.Set;

@RestController
@RequestMapping("/posts")
@AllArgsConstructor
public class PostController {
    private PostRepository postRepository;
    private TagRepository tagRepository;
    private UserProfileRepository userProfileRepository;
    private UserRepository userRepository;

    @GetMapping("/getall")
    public Page<Post> getAllPosts(Pageable pageable){
        return postRepository.findAll(pageable);
    }
    @GetMapping("/getbyid/{postId}")
    public Post getPostById(@PathVariable Long postId){
        return postRepository.findById(postId).orElseThrow(()->
                new NoSuchElementException("PostId" + postId + " not found"));
    }

    @GetMapping("/tag/{tagId}")
    public Set<Post> getAllPostsByTeg(@PathVariable Long tagId){
        Tag tag = tagRepository.findById(tagId).get();
        return tag.getPosts();
    }
    @PostMapping("/{postId}/{tagId}")
    public Post addPostAndTag(@PathVariable Long postId,
                              @PathVariable Long tagId){
        Post post = postRepository.findById(postId).get();
        Tag tag = tagRepository.findById(tagId).get();
        post.addTag(tag);
        tag.addPost(post);
        return postRepository.save(post);
    }

    @PostMapping("/{userId}/")
    public Post createPost(@PathVariable Long userId,
                           @Valid @RequestBody Post post){
        User user = userRepository.findById(userId).get();
        UserProfile userProfile = user.getUserProfile();
        post.setUserProfile(userProfile);
        return postRepository.save(post);
    }

    @PutMapping("/{postId}")
    public Post updatePost(@PathVariable Long postId,
                           @Valid @RequestBody Post updatePost){
        return postRepository.findById(postId).map(post -> {
            post.setTitle(updatePost.getTitle());
            post.setContent(updatePost.getContent());
            post.setDescription(updatePost.getDescription());
            return postRepository.save(post);
        }).orElseThrow(() -> new NoSuchElementException("PostId" + postId + " not found"));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity deletePost(@PathVariable Long postId){
        return postRepository.findById(postId).map(post -> {
            postRepository.delete(post);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new NoSuchElementException("PostId " + postId + " not found"));
    }

    @GetMapping("/tags/{tagId}")
    public Page<Post> getAllPostByTagId(@PathVariable Long tagId, Pageable pageable){
        return postRepository.findByTagsId(tagId, pageable);
    }
    @PostMapping("/tags/{tagId}")
    public Post createPostWithTagId(@PathVariable Long tagId,
                                    @Valid @RequestBody Post post){
        return tagRepository.findById(tagId).map(tag -> {
            post.addTag(tag);
            return postRepository.save(post);
        }).orElseThrow(() -> new NoSuchElementException("TagId " + tagId + " not found"));
    }
    @PutMapping("/{postId}/tags/{tagId}")
    public Post updatePostWithTag(@PathVariable Long postId,
                                  @PathVariable Long tagId,
                                  @Valid @RequestBody Post postUpdate){
        if(!tagRepository.existsById(tagId)){
            throw new NoSuchElementException("TagId " + tagId + " not found");
        }
        return postRepository.findById(postId).map(post -> {
            post.setTitle(postUpdate.getTitle());
            post.setDescription(postUpdate.getDescription());
            post.setContent(postUpdate.getContent());
            return postRepository.save(post);
        }).orElseThrow(() -> new NoSuchElementException("PostId " + postId + " not found"));
    }
    @Transactional
    @DeleteMapping("/dell/{userprofileId}")
    public ResponseEntity deleteAllPostsUserProfile(@PathVariable Long userprofileId){
        postRepository.deleteByUserProfileId(userprofileId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{postId}/tags/{tagId}")
    public ResponseEntity deletePostWithTags(@PathVariable Long postId,
                                             @PathVariable Long tagId){
        return postRepository.findByIdAndTagsId(postId, tagId).map(post -> {
            postRepository.delete(post);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new NoSuchElementException("PostId " + postId + " not found"));
    }
}
