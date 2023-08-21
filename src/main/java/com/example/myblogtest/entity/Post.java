package com.example.myblogtest.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "posts")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(unique = true)
    private String title;

    @NotNull
    @Size(max = 250)
    private String description;

    @NotNull
    @Lob
    private String content;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "post")
    @JsonIgnore
    private Set<Comment> comments = new HashSet<>();

    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(fetch = FetchType.LAZY,
                cascade = {
                           CascadeType.PERSIST,
                            CascadeType.MERGE
                })
    @JoinTable(name = "post_tags",
               joinColumns = {@JoinColumn(name = "post_id")},
                inverseJoinColumns = {@JoinColumn(name = "tag_id")})
    private Set<Tag> tags = new HashSet<>();

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_profile_id")
    private UserProfile userProfile;
    public void addTag(Tag tag){
        tags.add(tag);
        tag.getPosts().add(this);
    }
    public void removeTag(Tag tag){
        tags.remove(tag);
        tag.getPosts().remove(this);
    }
}
