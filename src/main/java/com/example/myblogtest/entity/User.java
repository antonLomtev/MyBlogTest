package com.example.myblogtest.entity;

import com.example.myblogtest.entity.enums.Priority;
import com.example.myblogtest.entity.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 65)
    private String firstName;

    @Size(max = 65)
    private String lastName;

    @NotNull
    @Email
    @Size(max = 100)
    @Column(unique = true)
    private String email;

    @NotNull
    @Size(max = 128)
    private String password;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToOne(fetch = FetchType.LAZY,
               cascade = CascadeType.ALL,
               mappedBy = "user")
    private UserProfile userProfile;
    @Enumerated(EnumType.ORDINAL)
    private Status status;
    @Enumerated(EnumType.STRING)
    private Status status2;

    @Basic
    private int priorityValue;
    @Transient
    @JsonIgnore
    private Priority priority;
    @PostLoad
    void fillTransient(){
        if(priorityValue > 0) {
            this.priority = Priority.of(priorityValue);
        }
    }
    @PrePersist
    void fillPersist(){
        if(priority != null) {
            this.priorityValue = priority.getPriority();
        }
    }
}
