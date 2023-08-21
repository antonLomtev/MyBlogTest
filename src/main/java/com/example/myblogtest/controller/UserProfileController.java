package com.example.myblogtest.controller;

import com.example.myblogtest.entity.UserProfile;
import com.example.myblogtest.repository.UserProfileRepository;
import com.example.myblogtest.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
public class UserProfileController {
    private UserProfileRepository userProfileRepository;
    private UserRepository userRepository;

    @GetMapping("/users/{userId}/userprofile")
    public List<UserProfile> getUser(@PathVariable Long userId){
        return userProfileRepository.findByUserId(userId);
    }
    @PostMapping("/users/{userId}/userprofile")
    public UserProfile createUser(@PathVariable Long userId,
                                  @RequestBody UserProfile createUserProfile){
        return userRepository.findById(userId).map(user -> {
            createUserProfile.setUser(user);
            return userProfileRepository.save(createUserProfile);
        }).orElseThrow();
    }
    @PostMapping("/upload")
    public void upload(@RequestParam("file") MultipartFile multipartFile){
        System.out.println("multipartfile");
    }
    @PutMapping("/users/{userId}/userprofile/{userprofileId}")
    public UserProfile updateUserProfile(@PathVariable Long userId,
                                         @PathVariable Long userprofileId,
                                         @RequestBody UserProfile updateUserProfile){
        if(!userRepository.existsById(userId)){
            throw new RuntimeException();
        }
        return userProfileRepository.findById(userprofileId).map(userProfile -> {
            userProfile.setPhoneNumber(updateUserProfile.getPhoneNumber());
            return userProfileRepository.save(userProfile);
        }).orElseThrow();
    }
}
