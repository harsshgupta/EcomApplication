package com.project.ecomapplication.dto;

import com.project.ecomapplication.customvalidations.PasswordMatchesForSeller;
import com.project.ecomapplication.customvalidations.ValidPassword;
import lombok.Data;
import javax.validation.constraints.*;

@Data
@PasswordMatchesForSeller
public class SignupSellerDao {
    private final String firstName;
    private final String lastName;

    @NotNull
    @Pattern(regexp="(^$|[0-9]{10})", message = "Phone number must be of 10 digits")
    @NotBlank(message = "Phone number cannot be empty")
    private final String companyContact;

    @NotNull
    @Email(flags = Pattern.Flag.CASE_INSENSITIVE, message = "Email should be unique and valid")
    @NotBlank(message = "Email cannot be empty")
    private final String email;

    @NotNull
    @ValidPassword
    private final String password;

    @NotNull
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, max = 16, message = "Password must be equal or greater than 8 characters but less than 16 characters")
    private final String confirmPassword;

    @NotNull
    @NotBlank(message = "Company name cannot be empty")
    @Pattern(regexp = "^[a-zA-Z0-9 ]*$", flags = Pattern.Flag.CASE_INSENSITIVE, message = "Company name should be unique")
    private final String companyName;

    @NotNull
    @Size(min = 15, max = 15)
    @Pattern(regexp = "\\d{2}[A-Z]{5}\\d{4}[A-Z]{1}[A-Z\\d]{1}[Z]{1}[A-Z\\d]{1}", message = "GST number should be according to Indian Govt. norms")
    @NotBlank(message = "GST number cannot be empty")
    private final String gstNumber;
}