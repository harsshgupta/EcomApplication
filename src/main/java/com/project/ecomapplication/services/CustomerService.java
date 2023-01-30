package com.project.ecomapplication.services;

import com.project.ecomapplication.dto.request.AddAddressDto;
import com.project.ecomapplication.dto.request.ChangePasswordDto;
import com.project.ecomapplication.dto.request.UpdateCustomerDto;
import com.project.ecomapplication.entities.Address;
import com.project.ecomapplication.entities.Customer;
import com.project.ecomapplication.entities.User;
import com.project.ecomapplication.repository.AccessTokenRepository;
import com.project.ecomapplication.repository.AddressRepository;
import com.project.ecomapplication.repository.CustomerRepository;
import com.project.ecomapplication.repository.UserRepository;
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

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@Slf4j
public class CustomerService {

    @Autowired
    AccessTokenRepository accessTokenRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;
    @Autowired
    MailSender mailSender;
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    HttpServletRequest request;

    public ResponseEntity<?> viewMyProfile(String name) {
//        AccessToken token = accessTokenRepository.findByToken(accessToken).orElseThrow(() -> new AccessDeniedException("Either the customer has logged out or the  token is invalid!"));
//        LocalDateTime expiredAt = token.getExpiresAt();
//        if (expiredAt.isBefore(LocalDateTime.now())) {
//            throw new TokenExpiredException("Access Token expired!!");
//        }
        User user = userRepository.findByEmail(name)
                .orElseThrow();
        return new ResponseEntity<>(customerRepository.findById(user.getId()), HttpStatus.OK);
    }


    public ResponseEntity<?> changePassword(ChangePasswordDto changePasswordDto, String name) {
        /*String token = changePasswordDto.getAccessToken();
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

    public ResponseEntity<?> addNewAddress(AddAddressDto addAddressDto,
                                           String name) {
//        String token = addAddressDto.getAccessToken();
//        AccessToken accessToken = accessTokenRepository.findByToken(token).orElseThrow(() -> new IllegalStateException("Invalid Access Token!"));
//        LocalDateTime expiredAt = accessToken.getExpiresAt();
//        if (expiredAt.isBefore(LocalDateTime.now())) {
//            throw new TokenExpiredException("Access Token expired!!");
//        }
        if (userRepository.existsByEmail(name)) {
            User user = userRepository.findUserByEmail(name);
            log.info("user exists");
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
        } else {
            log.info("Failed to add address.");
            return new ResponseEntity<>("Unable to add address!", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> updateAddress(Long id, AddAddressDto addAddressDto) {
       /* String token = addAddressDto.getAccessToken();
        AccessToken accessToken = accessTokenRepository.findByToken(token).orElseThrow(() -> new IllegalStateException("Invalid Access Token!"));
        LocalDateTime expiredAt = accessToken.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Access Token expired!!");
        }*/
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
            log.info("No address exists");
            return new ResponseEntity<>(String.format("No address exists with address id: " + id), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> deleteAddress(Long id) {
//        AccessToken token = accessTokenRepository.findByToken(accessToken).orElseThrow(() -> new IllegalStateException("Invalid Access Token!"));
//        LocalDateTime expiredAt = token.getExpiresAt();
//        if (expiredAt.isBefore(LocalDateTime.now())) {
//            throw new TokenExpiredException("Access Token expired!!");
//        }
        if (addressRepository.existsById(id)) {
            log.info("Address exists.");
            addressRepository.deleteById(id);
            log.info("deletion successful");
            return new ResponseEntity<>("Deleted Address Successfully.", HttpStatus.OK);
        } else {
            log.info("deletion failed!");
            return new ResponseEntity<>(String.format("No address found with associating address id: ", id), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> viewMyAddresses(String name) {
       /* AccessToken token = accessTokenRepository.findByToken(accessToken).orElseThrow(() -> new IllegalStateException("Invalid Access Token!"));
        LocalDateTime expiredAt = token.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Access Token expired!!");
        }*/
        if (userRepository.existsByEmail(name)) {
            log.info("User exists!");
            User user = userRepository.findUserByEmail(name);
            List<Object[]> list = addressRepository.findByUserId(user.getId());
            log.info("returning a list of objects.");
            return new ResponseEntity<>(list, HttpStatus.OK);
        } else {
            log.info("Couldn't find address related to user!!!");
            return new ResponseEntity<>("Error fetching addresses", HttpStatus.NOT_FOUND);
        }
    }


    public ResponseEntity<?> updateMyProfile(UpdateCustomerDto updateCustomerDto, String name) {
       /* String token = updateCustomerDto.getAccessToken();
        AccessToken accessToken = accessTokenRepository.findByToken(token).orElseThrow(() -> new IllegalStateException("Invalid Access Token!"));
        LocalDateTime expiredAt = accessToken.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Access Token expired!!");
        }*/
        if (userRepository.existsByEmail(name)) {
            log.info("User exists.");
            User user = userRepository.findUserByEmail(name);
            user.setFirstName(updateCustomerDto.getFirstName());
            user.setLastName(updateCustomerDto.getLastName());
            user.setEmail(updateCustomerDto.getEmail());
            Customer customer = customerRepository.getCustomerByUserId(user.getId());
            customer.setContact(updateCustomerDto.getContact());
            userRepository.save(user);
            customerRepository.save(customer);
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

   /* public ResponseEntity<?> uploadImage(String accessToken, MultipartFile multipartFile) {
        AccessToken token = accessTokenRepository.findByToken(accessToken).orElseThrow(() -> new IllegalStateException("Invalid Access Token!"));
        LocalDateTime expiredAt = token.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Access Token expired!!");
        }
        if (userRepository.existsByEmail(token.getUser().getEmail())) {
            log.info("User exists.");
            User user = userRepository.findUserByEmail(token.getUser().getEmail());

            try {
                if (multipartFile.isEmpty()) {
                    log.info("Empty file");
                    return new ResponseEntity<>("Image cannot be empty!!", HttpStatus.BAD_REQUEST);
                }
                if (!multipartFile.getContentType().equals("image/jpeg") &&
                        !multipartFile.getContentType().equals("image/png") &&
                        !multipartFile.getContentType().equals("image/bmp")) {

                    log.info("Illegal image format.");
                    return new ResponseEntity<>("Only JPEG,JPG,PNG & BMP extensions are allowed!!", HttpStatus.BAD_REQUEST);
                }
                String fileName = multipartFile.getOriginalFilename();
                log.info("Old name: " + fileName);
                String[] fileNameSplits = fileName.split("\\.");
                int extensionIndex = fileNameSplits.length - 1;
                String newName = user.getId()+"."+fileNameSplits[extensionIndex];
                log.info("New name: " + newName);

                //Check if profile image already exists

//                File file = new File(FileUploaderForUsersUtil.UPLOAD_DIR);
//                File[] files = file.listFiles((dir1, name) -> name.startsWith(user.getId().toString()));


                //1. Read file

                //2. Check if file's name starts with or contains user's id

                //3. Delete existing file

                //4. Copy new image


//                if (Files.deleteIfExists(Paths.get(FileUploaderForUsersUtil.UPLOAD_DIR+File.separator+newName))) {
//                    log.info("deleted duplicate image");
//                } else {
//                    Files.copy(multipartFile.getInputStream(), Paths.get(FileUploaderForUsersUtil.UPLOAD_DIR+File.separator+newName), StandardCopyOption.REPLACE_EXISTING);
//                }
                Files.copy(multipartFile.getInputStream(), Paths.get(FileUploaderForUsersUtil.UPLOAD_DIR+ File.separator+newName), StandardCopyOption.REPLACE_EXISTING);

                log.info("Image uploaded!");
                return new ResponseEntity<>("Updated the profile with latest image provided.", HttpStatus.OK);
            } catch (Exception exception) {
                log.error("Cannot upload image!");
                return new ResponseEntity<>("Cannot upload Image!", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("Could not load the image!", HttpStatus.BAD_REQUEST);
        }
    }
} */
}