package com.example.myblogtest.controller;

import com.example.myblogtest.entity.UserProfile;
import com.example.myblogtest.payload.UploadFileUserProfile;
import com.example.myblogtest.repository.UserProfileRepository;
import com.example.myblogtest.service.DBUserProfileStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class FileController {
    @Autowired
    private DBUserProfileStorage dbUserProfileStorage;
    @Autowired
    UserProfileRepository userProfileRepository;
    @PostMapping("/uploadFile/{userId}")
    public UploadFileUserProfile uploadFileByUserProfile(@PathVariable Long userId,
                                                         @RequestParam("file")MultipartFile file){
        UserProfile userProfile = dbUserProfileStorage.storeUserProfile(file, userId);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(userProfile.getId().toString())
                .toUriString();
        return new UploadFileUserProfile(userProfile.getCity(),fileDownloadUri,file.getContentType(),file.getSize());
    }
    @PostMapping("/uploadFile1/{userId}")
    public UploadFileUserProfile uploadFileByUserProfile1(@PathVariable Long userId,
                                                         @RequestParam("file")MultipartFile file) {
        UserProfile userProfile = dbUserProfileStorage.storeUserProfile(file, userId);
        userProfile.setImageUri(dbUserProfileStorage.storeFile(file));
        userProfileRepository.save(userProfile);
        return new UploadFileUserProfile(userProfile.getCity(),userProfile.getImageUri(),file.getContentType(),file.getSize());
    }
    @GetMapping("/downloadFile/{userId}")
    public ResponseEntity downloadFile(@PathVariable Long userId){
        UserProfile userProfile = dbUserProfileStorage.getFileUserProfile(userId);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(new ByteArrayResource(userProfile.getData()));
    }
}
