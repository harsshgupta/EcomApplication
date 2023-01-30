package com.project.ecomapplication.controller;

import com.project.ecomapplication.dto.LoginDao;
import com.project.ecomapplication.dto.request.TokenRefreshRequest;
import com.project.ecomapplication.dto.SignupCustomerDao;
import com.project.ecomapplication.dto.SignupSellerDao;
import com.project.ecomapplication.exceptions.ObjectNotFoundException;
import com.project.ecomapplication.exceptions.TokenRefreshException;
import com.project.ecomapplication.entities.*;
import com.project.ecomapplication.registrationconfig.RegistrationService;
import com.project.ecomapplication.repository.*;
import com.project.ecomapplication.security.JwtUtils;
import com.project.ecomapplication.services.EmailService;
import com.project.ecomapplication.services.RefreshTokenService;
import com.project.ecomapplication.services.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class PublicController {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    SellerRepository sellerRepository;
    @Autowired
    RegistrationService registrationService;
    @Autowired
    EmailService emailService;
    @Autowired
    RefreshTokenService refreshTokenService;
    @Autowired
    MailSender mailSender;
    @Autowired
    RefreshTokenRepository refreshTokenRepository;
    @Autowired
    AccessTokenRepository accessTokenRepository;

    Logger logger = LoggerFactory.getLogger(PublicController.class);

    @GetMapping("/home")
    public ResponseEntity<?> welcomeHome() {
        return ResponseEntity.ok("Welcome to EcommerceSite!!");
    }


    @PostMapping("/register/customer")
    public ResponseEntity<?> registerAsCustomer(@Valid @RequestBody SignupCustomerDao signupCustomerDao) {
        System.out.println("signupCustomerDao----"+signupCustomerDao);

        if (userRepository.existsByEmail(signupCustomerDao.getEmail())) {
            return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
        }

        Customer user = new Customer();
        user.setFirstName(signupCustomerDao.getFirstName());
        user.setEmail(signupCustomerDao.getEmail());
        user.setContact(signupCustomerDao.getContact());
        user.setPassword(passwordEncoder.encode(signupCustomerDao.getPassword()));
        user.setLastName(signupCustomerDao.getLastName());
        user.setIsActive(false);
        user.setIsDeleted(false);
        user.setIsExpired(false);
        user.setIsLocked(false);
        user.setInvalidAttemptCount(0);

        System.out.println("user----"+user);


        Roles roles = roleRepository.findByAuthority("ROLE_CUSTOMER").get();
        logger.info("roles----",roles);

        user.setRoles(Collections.singletonList(roles));

        logger.info("user after roles----",user);

        customerRepository.save(user);

        System.out.printf(user.getPassword());
        String token = registrationService.generateToken(user);
        emailService.setSubject("Your Account || " + user.getFirstName() + " finish setting up your new  Account ");

        emailService.setToEmail(user.getEmail());
        emailService.setMessage("Click on the link to Activate Your Account \n"
                + "http://localhost:8080/api/auth/confirm/customer?token=" + token );

        logger.info("------------" + token + "-----------------");
        emailService.sendEmail();


        return new ResponseEntity<>("Customer Registered Successfully!Activate Your Account within 3 hours", HttpStatus.CREATED);
    }

    @PostMapping("/register/seller")
    public ResponseEntity<?> registerAsSeller(@Valid @RequestBody SignupSellerDao signupSellerDao) {
        if (userRepository.existsByEmail(signupSellerDao.getEmail())) {
            return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
        }

        Seller user = new Seller();
        user.setFirstName(signupSellerDao.getFirstName());
        user.setEmail(signupSellerDao.getEmail());
        user.setPassword(passwordEncoder.encode(signupSellerDao.getPassword()));
        user.setLastName(signupSellerDao.getLastName());
        user.setCompanyContact(signupSellerDao.getCompanyContact());
        user.setGstNumber(signupSellerDao.getGstNumber());
        user.setCompanyName(signupSellerDao.getCompanyName());
        user.setIsActive(false);
        user.setIsDeleted(false);
        user.setIsExpired(false);
        user.setIsLocked(false);
        user.setInvalidAttemptCount(0);


        Roles roles = roleRepository.findByAuthority("ROLE_SELLER").get();
        user.setRoles(Collections.singletonList(roles));
        sellerRepository.save(user);
        String token = registrationService.generateToken(user);

        //Custom email testing part
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject("Account Created");
        mailMessage.setText("Congratulations, Your account has been created as Seller.\nContact Admin to activate your account, Thanks.");
        mailMessage.setTo(user.getEmail());
        mailMessage.setFrom("yourharshh@gmail.com");
        Date date = new Date();
        mailMessage.setSentDate(date);
        try {
            mailSender.send(mailMessage);
        } catch (MailException e) {
            log.info("Error sending mail");
        }

        return new ResponseEntity<>(
                "Seller Registered Successfully!\nYour account is under approval process from Admin!",
                HttpStatus.CREATED);
    }


    @PostMapping("/admin/login")
    public ResponseEntity<?> loginAsAdmin(@Valid @RequestBody LoginDao loginDao) {

        User user = userRepository.findUserByEmail(loginDao.getEmail());

        if (userRepository.isUserActive(user.getId())) {

            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginDao.getEmail(), loginDao.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            AccessToken accessToken = new AccessToken(jwtUtils.generateJwtToken(userDetails), LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), user);
            accessTokenRepository.save(accessToken);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
            refreshTokenRepository.save(refreshToken);
            String welcomeMessage = "Admin logged in Successfully!!";
            return new ResponseEntity<>(welcomeMessage + "\nAccess Token: " + accessToken.getToken() + "\nRefresh Token: " + refreshToken.getToken(), HttpStatus.OK);

        } else {

            return new ResponseEntity<>("Account is not activated, you cannot login!", HttpStatus.BAD_REQUEST);

        }

    }

    @PostMapping("/customer/login")
    public ResponseEntity<?> loginAsCustomer(@Valid @RequestBody LoginDao loginDao) {

        User user = userRepository.findByEmail(loginDao.getEmail()).orElseThrow(()->new ObjectNotFoundException("User Not Found"));

        if (userRepository.isUserActive(user.getId())) {

            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginDao.getEmail(), loginDao.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            AccessToken accessToken = new AccessToken(jwtUtils.generateJwtToken(userDetails), LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), user);
            accessTokenRepository.save(accessToken);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
            refreshTokenRepository.save(refreshToken);
            String welcomeMessage = "Customer logged in Successfully!!";
            return new ResponseEntity<>(welcomeMessage + "\nAccess Token: " + accessToken.getToken() + "\nRefresh Token: " + refreshToken.getToken(), HttpStatus.OK);

        } else {

            return new ResponseEntity<>("Account is not activated, you cannot login!", HttpStatus.BAD_REQUEST);

        }
    }

    @PostMapping("/seller/login")
    public ResponseEntity<?> loginAsSeller(@Valid @RequestBody LoginDao loginDao) {

        User user = userRepository.findUserByEmail(loginDao.getEmail());

//        return new ResponseEntity<User>(user,HttpStatus.OK);

        if (userRepository.isUserActive(user.getId())) {

            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginDao.getEmail(), loginDao.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            AccessToken accessToken = new AccessToken(jwtUtils.generateJwtToken(userDetails), LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), user);
            accessTokenRepository.save(accessToken);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
            refreshTokenRepository.save(refreshToken);
            String welcomeMessage = "Seller logged in Successfully!!";
            return new ResponseEntity<>(welcomeMessage + "\nAccess Token: " + accessToken.getToken() + "\nRefresh Token: " + refreshToken.getToken(), HttpStatus.OK);

        } else {

            return new ResponseEntity<>("Account is not activated, you cannot login!", HttpStatus.BAD_REQUEST);

        }

    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getEmail());
                    AccessToken accessToken = new AccessToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), user);
                    accessTokenRepository.save(accessToken);
                    return new ResponseEntity<>("New Access Token: " + accessToken.getToken() + "\nRefresh Token: " + requestRefreshToken, HttpStatus.OK);
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }

    @PatchMapping(path = "/confirm/customer")
    public String confirm(String token) {
        return registrationService.confirmToken(token);
    }

    @PostMapping(path = "/resendlink/customer")
    public String confirmByEmail(@RequestParam("email") String email) {
        return registrationService.confirmByEmail(email);
    }

}


