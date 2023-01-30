package com.project.ecomapplication.dto.request;

import javax.validation.constraints.*;

public class TokenRefreshRequest {

    @NotNull
    @NotBlank
    private String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}