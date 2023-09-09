package com.example.myblogtest.service;

import com.example.myblogtest.entity.UserProfile;
import com.example.myblogtest.repository.UserProfileRepository;
import com.example.myblogtest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.NoSuchElementException;

@Service
public class DBUserProfileStorage {
    @Autowired
    private UserProfileRepository userProfileRepository;
    @Autowired
    private UserRepository userRepository;
    private final Path fileStorageLocation = Paths.get("downloads")
            .toAbsolutePath().normalize();


    {
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {

        }
    }
    public String storeFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new NoSuchElementException ("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {

        }
        return fileName;
    }

    public UserProfile storeUserProfile(MultipartFile file, Long userId){
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try{
            if(fileName.contains("..")){
                throw new NoSuchElementException("Uncorrected fileName");
            }
            UserProfile userProfile = userRepository.findById(userId).get().getUserProfile();
            userProfile.setData(file.getBytes());
            return userProfileRepository.save(userProfile);
        }catch (IOException ex){
            throw new NoSuchElementException();
        }
    }
    public Resource loadFileAsResource(String fileName){
        try{
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()){
                return resource;
            }else {
                throw new NoSuchElementException();
            }
        }catch (MalformedURLException exception){
            throw new NoSuchElementException();
        }
    }
    public UserProfile getFileUserProfile(Long userId){
        return userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException())
                .getUserProfile();
    }
}
