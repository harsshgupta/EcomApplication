package com.project.ecomapplication.dto.request;

import com.project.ecomapplication.customvalidations.ValidPassword;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.*;


@Data
public class UpdateCustomerDto {


    @NotNull
    @NotBlank(message = "First Name cannot be empty")
    private String firstName;

    @NotNull
    @NotBlank(message = "Last Name cannot be empty")
    private String lastName;

    @NotNull
    @NotBlank
    @Valid
    @ValidPassword
    private String password;

    @NotNull
    @Pattern(regexp="(^$|[0-9]{10})", message = "Phone number must be of 10 digits")
    @NotBlank(message = "Phone number cannot be empty")
    private String contact;

    @NotNull
    @Email(flags = Pattern.Flag.CASE_INSENSITIVE, message = "Email should be unique and valid")
    @NotBlank(message = "Email cannot be empty")
    private String email;
}