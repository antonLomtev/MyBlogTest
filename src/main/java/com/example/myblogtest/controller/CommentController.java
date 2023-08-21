package com.example.myblogtest.controller;

import com.example.myblogtest.entity.Comment;
import com.example.myblogtest.entity.User;
import com.example.myblogtest.repository.CommentRepository;
import com.example.myblogtest.repository.PostRepository;
import com.example.myblogtest.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/comments")
@AllArgsConstructor
public class CommentController {
    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private UserRepository userRepository;

    @GetMapping("/getall")
    public Page<Comment> getAllComments(Pageable pageable){
        return commentRepository.findAll(pageable);
    }
    @GetMapping("/getbyid/{commentId}")
    public Comment getCommentById(@PathVariable Long commentId){
        return commentRepository.findById(commentId).orElseThrow(() -> {
           return new NoSuchElementException("Comment " + commentId + " not found");
        });
    }
    @GetMapping("/posts/{postId}")
    public Page<Comment> getAllCommentsByPost(@PathVariable Long postId, Pageable pageable){
        return commentRepository.findByPostId(postId, pageable);
    }

    @PostMapping("/posts/{postId}/{userId}")
    public Comment createComment(@PathVariable Long postId,
                                 @PathVariable Long userId,
                                 @Valid @RequestBody Comment comment){
        return postRepository.findById(postId).map(post -> {
            User user = userRepository.findById(userId).get();
            comment.setUser(user);
            comment.setPost(post);
            return commentRepository.save(comment);
        }).orElseThrow(() -> new NoSuchElementException("Post " + postId + " not found"));
    }

    @PostMapping

    @PutMapping("/posts/{postId}/comments/{commentId}")
    public Comment updateComment(@PathVariable Long postId,
                                 @PathVariable Long commentId,
                                 @Valid @RequestBody Comment commentRequest){
        if(!postRepository.existsById(postId)){
            throw new NoSuchElementException("PostId " + postId + " not found");
        }
        return commentRepository.findById(commentId).map(comment -> {
            comment.setText(commentRequest.getText());
            return commentRepository.save(comment);
        }).orElseThrow(() -> new NoSuchElementException("CommentId" + commentId + " not found"));
    }

    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity deleteComment(@PathVariable Long postId,
                                        @PathVariable Long commentId){
        return commentRepository.findByIdAndPostId(commentId, postId).map(comment -> {
            commentRepository.delete(comment);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new NoSuchElementException("CommentId" + commentId + " not found"));
    }
}
