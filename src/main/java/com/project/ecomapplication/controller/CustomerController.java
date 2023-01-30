package com.project.ecomapplication.controller;

import com.project.ecomapplication.dto.request.AddAddressDto;
import com.project.ecomapplication.dto.request.ChangePasswordDto;
import com.project.ecomapplication.dto.request.UpdateCustomerDto;
import com.project.ecomapplication.repository.CategoryRepository;
import com.project.ecomapplication.services.CategoryService;
import com.project.ecomapplication.services.CustomerService;
import com.project.ecomapplication.services.ProfilePhotoService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;


@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @Autowired
    ProfilePhotoService profilePhotoService;

    @Autowired
    CategoryService categoryService;

    @Value("${harsh.app.jwtSecret}")
    private String jwtSecret;
    @GetMapping("/my-profile")
    public ResponseEntity<?> viewMyProfile(@Valid Authentication authentication) {
        return customerService.viewMyProfile(authentication.getName());
    }

    @PostMapping("/add-address")
    public ResponseEntity<?> addNewAddress(@Valid Authentication authentication,
                                           @Valid @RequestBody AddAddressDto addAddressDto) {
        return customerService.addNewAddress(addAddressDto,
                authentication.getName());
    }

    @PutMapping("/update-address")
    public ResponseEntity<?> updateAddress(@Valid Authentication authentication, @RequestParam("addressId") Long id, @RequestBody AddAddressDto addAddressDto) {
        return customerService.updateAddress(id, addAddressDto);
    }

    @DeleteMapping("/delete-address")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<?> deleteAddress(@Valid Authentication authentication, @RequestParam("addressId") Long id) {
        return customerService.deleteAddress(id);
    }

    @GetMapping("/my-addresses")
    public ResponseEntity<?> viewMyAddresses(@Valid Authentication authentication) {
        return customerService.viewMyAddresses(authentication.getName());
    }


    @PutMapping("/change-password")
    public ResponseEntity<?> changeMyPassword(@Valid @RequestBody ChangePasswordDto changePasswordDto,
                                              Authentication authentication) {
        return customerService.changePassword(changePasswordDto, authentication.getName());
    }


    @PutMapping("/update-profile")
    public ResponseEntity<?> updateProfile(@Valid @RequestBody UpdateCustomerDto updateCustomerDto, Authentication authentication) {
        return customerService.updateMyProfile(updateCustomerDto, authentication.getName());
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
