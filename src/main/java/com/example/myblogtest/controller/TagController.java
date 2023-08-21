package com.example.myblogtest.controller;

import com.example.myblogtest.entity.Post;
import com.example.myblogtest.entity.Tag;
import com.example.myblogtest.repository.PostRepository;
import com.example.myblogtest.repository.TagRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@RestController
@RequestMapping("/tag")
@AllArgsConstructor
public class TagController {
    private TagRepository tagRepository;
    private PostRepository postRepository;
    @GetMapping("/getall")
    public Page<Tag> getAllTags(Pageable pageable){
        return tagRepository.findAll(pageable);
    }
    @GetMapping("/getbyid/{tagId}")
    public Tag getTagById(@PathVariable Long tagId){
        return tagRepository.findById(tagId).orElseThrow(() -> {
           return new NoSuchElementException("TagId " + tagId + " not found");
        });
    }
    @GetMapping("/posts/{postId}")
    public Page<Tag> getAllTagsByPostId(@PathVariable Long postId,
                                        Pageable pageable){
        return tagRepository.findByPostsId(postId, pageable);
    }
    @GetMapping("/alltegspost/{postId}")
    public Set<Tag> getAllTegsPost(@PathVariable Long postId){
        Post post = postRepository.findById(postId).get();
        return post.getTags();

    }
    @PostMapping("/posts/{postId}")
    public Tag createTagWithPostId(@PathVariable Long postId,
                                   @Valid @RequestBody Tag tag){
        return postRepository.findById(postId).map(post -> {
            tag.addPost(post);
            return tagRepository.save(tag);
        }).orElseThrow(() -> new NoSuchElementException("PostId " + postId + " not found"));
    }
    @PutMapping("/posts/{postId}/tags/{tagId}")
    public Tag updateTag(@PathVariable Long postId,
                         @PathVariable Long tagId,
                         @Valid @RequestBody Tag updateTag){
        if(!postRepository.existsById(postId)){
            throw new NoSuchElementException("PostId " + postId + " not found");
        }
        return tagRepository.findById(tagId).map(tag -> {
            tag.setName(updateTag.getName());
            return tagRepository.save(tag);
        }).orElseThrow(() -> new NoSuchElementException("TagId " + tagId + " not found"));
    }
    @DeleteMapping("/posts/{postId}/tags/{tagId}")
    public ResponseEntity deleteTag(@PathVariable Long postId,
                                    @PathVariable Long tagId){
        return tagRepository.findByIdAndPostsId(tagId, postId).map(tag -> {
            tagRepository.delete(tag);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new NoSuchElementException("TagId " + tagId + " not found"));
    }
    @Transactional
    @DeleteMapping("/posts/{postId}/tagsdelete/{tagId}")
    public ResponseEntity deleteTagWithPost(@PathVariable Long postId,
                                            @PathVariable Long tagId){
        Post post = postRepository.findById(postId).get();
        Tag tag = tagRepository.findById(tagId).get();
        post.removeTag(tag);
        tag.removePost(post);
//        postRepository.save(post);
        tagRepository.save(tag);
        return ResponseEntity.ok().build();
    }
}
