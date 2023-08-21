package com.example.myblogtest.controller;

import com.example.myblogtest.entity.Comment;
import com.example.myblogtest.entity.Post;
import com.example.myblogtest.entity.User;
import com.example.myblogtest.repository.CommentRepository;
import com.example.myblogtest.repository.PostRepository;
import com.example.myblogtest.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private UserRepository userRepository;
    private PostRepository postRepository;
    private CommentRepository commentRepository;

    @GetMapping("/getall")
    public Page<User> getAllUsers(Pageable pageable){
        return userRepository.findAll(pageable);
    }
    @GetMapping("/getbyid/{userId}")
    public User getUserById(@PathVariable Long userId){
        return userRepository.findById(userId).orElseThrow(() -> {
            return new NoSuchElementException("USerId " + userId + " not found");
        });
    }
    @GetMapping("/{userprofileId}")
    public Page<Post> getAllPostsUserProfile(@PathVariable Long userprofileId,
                                             Pageable pageable){
        return postRepository.findByUserProfileId(userprofileId,pageable);
    }

    @GetMapping("/getallcomments/{userId}")
    public Page<Comment> getAllCommentUser(@PathVariable Long userId,
                                           Pageable pageable){
        return commentRepository.findByUserId(userId,pageable);
    }
    @GetMapping("/allcommentspost/{postId}/{userId}")
    public Page<Comment> getAllCommentsUserByPost(@PathVariable Long postId,
                                                  @PathVariable Long userId,
                                                  Pageable pageable){
        return commentRepository.findByPostIdAndUserId(postId, userId, pageable);
    }

    @PostMapping("/")
    public User createUser(@RequestBody User createUser){

        return userRepository.save(createUser);
    }
    @PutMapping("/{userId}")
    public User updateUser(@PathVariable Long userId,
                           @RequestBody User updateUser){
        return userRepository.findById(userId).map(user -> {
            user.setFirstName(updateUser.getFirstName());
            user.setLastName(user.getLastName());
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException());
    }
    @DeleteMapping("/users/{userId}")
    public ResponseEntity deleteUser(@PathVariable Long userId){
        return userRepository.findById(userId).map(user -> {
            userRepository.delete(user);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new RuntimeException());
    }
}
