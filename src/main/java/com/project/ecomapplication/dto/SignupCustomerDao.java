package com.project.ecomapplication.dto;

import com.project.ecomapplication.customvalidations.PasswordMatchesForCustomer;
import com.project.ecomapplication.customvalidations.ValidPassword;
import lombok.Data;
import javax.validation.Valid;
import javax.validation.constraints.*;

@Data
@PasswordMatchesForCustomer
public class SignupCustomerDao {

    @NotNull
    @NotBlank(message = "First Name cannot be empty")
    private String firstName;

    @NotNull
    @NotBlank(message = "last name cannot be empty")
    private String lastName;

    @NotNull
    @Pattern(regexp="(^$|[0-9]{10})", message = "Phone number must be of 10 digits")
    @NotBlank(message = "Phone number cannot be empty")
    private String contact;

    @NotNull
    @Email(flags = Pattern.Flag.CASE_INSENSITIVE, message = "Email should be unique and valid")
    @NotBlank(message = "Email cannot be empty")
    private String email;

    @NotNull
    @Valid
    @ValidPassword
    private String password;

    @NotNull (message = "Password cannot be empty")
    @Size(min = 8, max = 16, message = "Password should be same to Password")
    private String confirmPassword;


}