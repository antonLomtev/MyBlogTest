package com.example.myblogtest.controller;

import com.example.myblogtest.entity.Comment;
import com.example.myblogtest.entity.Post;
import com.example.myblogtest.entity.User;
import com.example.myblogtest.entity.enums.Priority;
import com.example.myblogtest.entity.enums.Status;
import com.example.myblogtest.repository.CommentRepository;
import com.example.myblogtest.repository.PostRepository;
import com.example.myblogtest.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
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
    @PutMapping("/change/{userId}")
    public User changedUser(@PathVariable Long userId){
        User user = userRepository.findById(userId).get();
        user.setStatus2(Status.REVIEW);
        userRepository.save(user);
        return user;
    }
//    @GetMapping("/getuserstatus/")
//    public List<User> getUsersByStatus(@RequestParam String status2){
//        Status status = Status.valueOf(status2);
//        return  userRepository.getUsersForUserStatus2(status);
//    }
    @PutMapping("/chang1/{userID}/")
    public  User changeStatusUserId1(@PathVariable Long userID){
        User user = userRepository.findById(userID).get();
//        user.setPriority(Priority.HIGH);

        return userRepository.save(user);
    }
//    @PutMapping("/chang/{userID}/")
//    public  ResponseEntity changeStatusUserId(@PathVariable Long userID,
//                                    @RequestParam String status2){
//        Status status = Status.valueOf(status2);
//        userRepository.updateStatusUserId(userID, status);
//        return ResponseEntity.ok().build();
//    }
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
