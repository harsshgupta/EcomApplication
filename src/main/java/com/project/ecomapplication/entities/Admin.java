package com.project.ecomapplication.entities;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.*;

@Data
@Entity
@Table(name = "admin")
@AllArgsConstructor
@NoArgsConstructor
public class Admin {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Email(flags = Pattern.Flag.CASE_INSENSITIVE, message = "Email should be unique and valid")
    @NotBlank(message = "Email cannot be empty")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    @Size(message = "Enter correct password or else after 3rd attempt, account will be LOCKED!")
    private String password;

}