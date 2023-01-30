package com.project.ecomapplication.controller;

import com.project.ecomapplication.dto.request.ResetPasswordDto;
import com.project.ecomapplication.services.PasswordResetTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;

@RestController
@RequestMapping("/api/user")
public class ForgotPasswordController {

    @Autowired
    PasswordResetTokenService passwordResetTokenService;

    @GetMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Email @RequestParam String email) {
        return passwordResetTokenService.forgotPassword(email);
    }

    @PatchMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordDto resetPasswordDto) {
        return passwordResetTokenService.resetPassword(resetPasswordDto);
    }
}