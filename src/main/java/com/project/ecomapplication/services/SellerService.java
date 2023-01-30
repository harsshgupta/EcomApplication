package com.project.ecomapplication.services;

import com.project.ecomapplication.dto.request.AddAddressDto;
import com.project.ecomapplication.dto.request.ChangePasswordDto;
import com.project.ecomapplication.dto.request.UpdateSellerDto;
import com.project.ecomapplication.exceptions.ObjectNotFoundException;
import com.project.ecomapplication.entities.Address;
import com.project.ecomapplication.entities.Seller;
import com.project.ecomapplication.entities.User;
import com.project.ecomapplication.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;

@Service
@Slf4j
@Transactional
public class SellerService {

    @Autowired
    AccessTokenRepository accessTokenRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    SellerRepository sellerRepository;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;
    @Autowired
    MailSender mailSender;
    @Autowired
    AddressRepository addressRepository;

    public ResponseEntity<?> viewSellerProfile(String name) {
      /*  AccessToken token = accessTokenRepository.findByToken(accessToken).orElseThrow(() -> new IllegalStateException("Invalid Access Token!"));
        LocalDateTime expiredAt = token.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Access Token expired!!");
        }*/
        if (userRepository.existsByEmail(name)) {
            log.info("User exists!");
            User user = userRepository.findUserByEmail(name);
            log.info("returning a list of objects.");
            return new ResponseEntity<>(sellerRepository.findById(user.getId()), HttpStatus.OK);
        } else {
            log.info("Couldn't find address related to user!!!");
            return new ResponseEntity<>("Error fetching addresses", HttpStatus.NOT_FOUND);
        }

    }


    public ResponseEntity<?> updateSellerProfile(UpdateSellerDto updateSellerDto, String name) {
      /*   String token = updateSellerDto.getAccessToken();
        AccessToken accessToken = accessTokenRepository.findByToken(token).orElseThrow(() -> new IllegalStateException("Invalid Access Token!"));
        LocalDateTime expiredAt = accessToken.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Access Token expired!!");
        }*/
        if (userRepository.existsByEmail(name)) {
            log.info("User exists.");
            User user = userRepository.findUserByEmail(name);
            user.setFirstName(updateSellerDto.getFirstName());
            user.setLastName(updateSellerDto.getLastName());
            user.setEmail(updateSellerDto.getEmail());
            Seller seller = sellerRepository.getSellerByUserId(user.getId());
            seller.setCompanyContact(updateSellerDto.getCompanyContact());
            seller.setCompanyName(updateSellerDto.getCompanyName());
            seller.setGstNumber(updateSellerDto.getGstNumber());
            userRepository.save(user);
            sellerRepository.save(seller);
            log.info("user updated!");

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setSubject("Profile Updated");
            mailMessage.setText("ALERT!, Your profile has been updated, If it was not you contact Admin asap.\nStay Safe, Thanks.");
            mailMessage.setTo(user.getEmail());
            mailMessage.setFrom("yourharshh@gmail.com");
            Date date = new Date();
            mailMessage.setSentDate(date);
            try {
                mailSender.send(mailMessage);
            } catch (MailException e) {
                log.info("Error sending mail");
            }
            return new ResponseEntity<>("User profile updated!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Could not update the profile!", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> changeSellerPassword(ChangePasswordDto changePasswordDto, String name) {
     /*   String token = changePasswordDto.getAccessToken();
        AccessToken accessToken = accessTokenRepository.findByToken(token).orElseThrow(() -> new IllegalStateException("Invalid Access Token!"));
        LocalDateTime expiredAt = accessToken.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Access Token expired!!");
        }*/
        if (userRepository.existsByEmail(name)) {
            User user = userRepository.findUserByEmail(name);
            user.setPassword(passwordEncoder.encode(changePasswordDto.getPassword()));
            log.info("Changed password and encoded, then saved it.");
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setSubject("Password Changed");
            mailMessage.setText("ALERT!, Your account's password has been changed, If it was not you contact Admin asap.\nStay Safe, Thanks.");
            mailMessage.setTo(user.getEmail());
            mailMessage.setFrom("yourharshh@gmail.com");
            Date date = new Date();
            mailMessage.setSentDate(date);
            try {
                mailSender.send(mailMessage);
            } catch (MailException e) {
                log.info("Error sending mail");
            }
            return new ResponseEntity<>("Changed Password Successfully!", HttpStatus.OK);
        } else {
            log.info("Failed to change password!");
            return new ResponseEntity<>("Failed to change password!", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> updateSellerAddress(Long id, AddAddressDto addAddressDto, String name) {
       /* String token = addAddressDto.getAccessToken();
        AccessToken accessToken = accessTokenRepository.findByToken(token).orElseThrow(() -> new IllegalStateException("Invalid Access Token!"));
        LocalDateTime expiredAt = accessToken.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Access Token expired!!");
        }*/

        if (userRepository.existsByEmail(name)) {
            User user = userRepository.findUserByEmail(name);
            log.info("user exists");

            if (addressRepository.existsById(id)) {
                log.info("address exists");
                Address address = addressRepository.getById(id);
                address.setAddressLine(addAddressDto.getAddressLine());
                address.setLabel(addAddressDto.getLabel());
                address.setZipcode(addAddressDto.getZipcode());
                address.setCountry(addAddressDto.getCountry());
                address.setState(addAddressDto.getState());
                address.setCity(addAddressDto.getCity());
                log.info("trying to save the updated address");
                addressRepository.save(address);
                return new ResponseEntity<>("Address updated successfully.", HttpStatus.OK);
            } else {
                Address address = new Address();
                address.setUser(user);
                address.setAddressLine(addAddressDto.getAddressLine());
                address.setCity(addAddressDto.getCity());
                address.setCountry(addAddressDto.getCountry());
                address.setState(addAddressDto.getState());
                address.setZipcode(addAddressDto.getZipcode());
                address.setLabel(addAddressDto.getLabel());
                addressRepository.save(address);
                log.info("Address added to the respected user");
                return new ResponseEntity<>("Added the address.", HttpStatus.CREATED);
            }
        } else {
            log.info("No address exists");
            return new ResponseEntity<>(String.format("No address exists with address id: " + id), HttpStatus.NOT_FOUND);
        }


    }

    public String uploadImage(String email, MultipartFile multipartFile) throws IOException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ObjectNotFoundException("User Not Found"));
        String[] arr = multipartFile.getContentType().split("/");
        Path uploadPath = Paths.get("/home/harsh/ecommerce-picture");
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(user.getId() + "." + arr[1]);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save file ", ioe);
        }
        return "Image Uploaded Successfully";
    }

}

