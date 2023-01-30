package com.project.ecomapplication.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name="user")
@Inheritance(strategy = InheritanceType.JOINED)
public class User {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;
     private String email;
     private String firstName;
     private String lastName;
     private String password;
     private Boolean isDeleted;
     private Boolean isActive;
     private Boolean isExpired;
     private Boolean isLocked;
     private Integer invalidAttemptCount;
     private LocalDateTime passwordUpdateDate;
     private String passwordResetToken;
     private LocalDateTime tokenCreationDate;
     private int failedAttempt;

     //   private LocalDate passwordUpdateDate;

     @JsonManagedReference
     @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
     private List<Address> addresses;

     @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.MERGE)//want to fetch everything
     @JoinTable(name="user_role",joinColumns = @JoinColumn(name="userID" ,referencedColumnName = "id"),
             inverseJoinColumns = @JoinColumn(name="roleID",referencedColumnName = "id"))
     private List<Roles> roles;

     //@Lob
    // private byte[] image;

}
