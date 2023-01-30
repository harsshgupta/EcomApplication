package com.project.ecomapplication.controller;

import com.project.ecomapplication.dto.request.AddAddressDto;
import com.project.ecomapplication.dto.request.ChangePasswordDto;
import com.project.ecomapplication.dto.request.UpdateSellerDto;
import com.project.ecomapplication.repository.CategoryRepository;
import com.project.ecomapplication.services.CategoryService;
import com.project.ecomapplication.services.ProfilePhotoService;
import com.project.ecomapplication.services.SellerService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;


@RestController
@RequestMapping("/api/seller")
public class SellerController {

    @Autowired
    SellerService sellerService;

    @Autowired
    ProfilePhotoService profilePhotoService;

    @Value("${harsh.app.jwtSecret}")
    private String jwtSecret;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CategoryService categoryService;

    @GetMapping("/seller-profile")
    public ResponseEntity<?> viewSellerProfile(@Valid Authentication authentication) {
        return sellerService.viewSellerProfile(authentication.getName());
    }

    @PutMapping("/update-seller-profile")
    public ResponseEntity<?> updateSellerProfile(@Valid @RequestBody UpdateSellerDto updateSellerDto, Authentication authentication) {
        return sellerService.updateSellerProfile(updateSellerDto, authentication.getName());
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changeSellerPassword(@Valid @RequestBody ChangePasswordDto changePasswordDto, Authentication authentication) {
        return sellerService.changeSellerPassword(changePasswordDto, authentication.getName());
    }

    @PutMapping("/update-seller-address")
    public ResponseEntity<?> updateSellerAddress(@Valid@RequestParam("addressId") Long id, @RequestBody AddAddressDto addAddressDto, Authentication authentication) {
        return sellerService.updateSellerAddress(id, addAddressDto, authentication.getName());
    }

    @PostMapping(value = "upload-image")
    public String uploadImage(@RequestParam("image") MultipartFile image, HttpServletRequest request)
            throws IOException {
        String email = "";

        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            String token = bearerToken.substring(7);

            Claims claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token)
                    .getBody();
            email = claims.getSubject();
        }

        return profilePhotoService.uploadImage(email, image);

    }

    @GetMapping("/view-image")
    public ResponseEntity<?> listFilesUsingJavaIO(HttpServletRequest request) {

        String email = "";

        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            String token = bearerToken.substring(7);

            Claims claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token)
                    .getBody();
            email = claims.getSubject();
        }

        return profilePhotoService.getImage(email);
    }

    @GetMapping("/list-all-categories")
    public ResponseEntity<?> allCategories() {
        return categoryService.viewAllCategories();
    }

}


