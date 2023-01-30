package com.project.ecomapplication.dto.request;

import com.project.ecomapplication.customvalidations.PasswordMatchesForResetPasswordRequest;
import com.project.ecomapplication.customvalidations.ValidPassword;
import lombok.Data;
import javax.validation.constraints.*;


@Data
@PasswordMatchesForResetPasswordRequest
public class ResetPasswordDto {

    @NotNull
    @NotBlank(message = "Password Reset Token cannot be blank")
    private String token;

    @NotNull
    @ValidPassword
    private String password;

    @NotNull
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, max = 16, message = "Password should be same to Password")
    private String confirmPassword;
}