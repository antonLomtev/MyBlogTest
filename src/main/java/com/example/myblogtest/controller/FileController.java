package com.example.myblogtest.controller;

import com.example.myblogtest.entity.UserProfile;
import com.example.myblogtest.payload.UploadFileUserProfile;
import com.example.myblogtest.repository.UserProfileRepository;
import com.example.myblogtest.service.DBUserProfileStorage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;

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
        String fileName = dbUserProfileStorage.storeFile(file);
        String fileDownLoadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile1/")
                .path(fileName)
                .toUriString();
        userProfile.setImageUri(fileDownLoadUri);
        userProfileRepository.save(userProfile);
        return new UploadFileUserProfile(userProfile.getCity(), fileDownLoadUri, file.getContentType(), file.getSize());
    }
    @GetMapping("/downloadFile/{userId}")
    public ResponseEntity downloadFile(@PathVariable Long userId){
        UserProfile userProfile = dbUserProfileStorage.getFileUserProfile(userId);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(new ByteArrayResource(userProfile.getData()));
    }
    @GetMapping("/downloadFile1/{filename:.+}")
    public ResponseEntity<Resource> downloadFile1(@PathVariable String filename, HttpServletRequest request){
        Resource resource = dbUserProfileStorage.loadFileAsResource(filename);
        String contentType = null;
        try{
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        }catch (IOException exception){
            exception.printStackTrace();
        }
        if(contentType == null){
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }
}
