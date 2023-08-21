package com.example.myblogtest.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Size(max = 100)
    @NaturalId
    private String name;
    @ManyToMany(fetch = FetchType.LAZY,
                 cascade = {
                           CascadeType.PERSIST,
                           CascadeType.MERGE
                 },
                  mappedBy = "tags")
    @JsonIgnore
    private Set<Post> posts = new HashSet<>();

    public void addPost(Post post){
        posts.add(post);
        post.getTags().add(this);
    }
    public void removePost(Post post){
        posts.remove(post);
//        post.getTags().remove(this);
    }
}
