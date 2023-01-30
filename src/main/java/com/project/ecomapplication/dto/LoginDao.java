package com.project.ecomapplication.dto;

import lombok.Data;
import javax.validation.constraints.*;

@Data
public class LoginDao {

    @NotNull
    @Email(flags = Pattern.Flag.CASE_INSENSITIVE, message = "Email should be unique and valid")
    @NotBlank(message = "Email cannot be empty")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\S+$).{8,15}$")
    @Size(message = "Enter correct password or else after 3rd attempt, account will be LOCKED!")
    private String password;
}
