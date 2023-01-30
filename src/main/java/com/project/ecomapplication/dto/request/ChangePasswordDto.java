package com.project.ecomapplication.dto.request;

import com.project.ecomapplication.customvalidations.PasswordMatchesForChangePasswordRequest;
import com.project.ecomapplication.customvalidations.ValidPassword;
import lombok.Data;
import javax.validation.constraints.*;

@Data
@PasswordMatchesForChangePasswordRequest
public class ChangePasswordDto {

    @NotNull
    @ValidPassword
    private String password;

    @NotNull
    @NotBlank(message = "Confirm Password should be same to Password")
    private String confirmPassword;
}