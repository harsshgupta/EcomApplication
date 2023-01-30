package com.project.ecomapplication.controller;

import com.project.ecomapplication.entities.User;
import com.project.ecomapplication.registrationconfig.RegistrationService;
import com.project.ecomapplication.repository.UserRepository;
import com.project.ecomapplication.services.AccessTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/admin")
public class AdminController {


    @Autowired
    RegistrationService registrationService;

    @Autowired
    UserRepository userRepository;


    @GetMapping("/admin-board")
    public String adminAccess()

    {
        return "Admin Board.";
    }


    @GetMapping("/user-list")
    public @ResponseBody
    List<User> returnUsers()
    {
        return userRepository.findAll();
    }

    @GetMapping("/customer-list")
    public @ResponseBody List<Object[]> returnCustomers ()
    {
        List<Object[]> list = new ArrayList<>();
        list.addAll(userRepository.printPartialDataForCustomers());
        return list;
    }

    @GetMapping("/seller-list")
    public @ResponseBody List<Object[]> returnSellers() {
        List<Object[]> list = new ArrayList<>();
        list.addAll(userRepository.printPartialDataForSellers());
        return list;
    }

    @PatchMapping("/activate/customer/{id}")
    public ResponseEntity<?> activateCustomer(@PathVariable("id") Long id) {
        return registrationService.confirmById(id);
    }

    @PatchMapping("/deactivate/customer/{id}")
    public ResponseEntity<?> deactivateCustomer(@PathVariable("id") Long id) {
        return registrationService.disableById(id);
    }

    @PatchMapping("/activate/seller/{id}")
    public ResponseEntity<?> activateSeller(@PathVariable("id") Long id) {
        return registrationService.confirmById(id);
    }

    @PatchMapping("/deactivate/seller/{id}")
    public ResponseEntity<?> deactivateSeller(@PathVariable("id") Long id) {
        return registrationService.disableById(id);
    }
}
