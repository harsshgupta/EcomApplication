package com.project.ecomapplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.project.ecomapplication.entities.Address;
import com.project.ecomapplication.entities.Roles;
import com.project.ecomapplication.entities.User;
import com.project.ecomapplication.repository.RoleRepository;
import com.project.ecomapplication.repository.UserRepository;
import java.util.*;

@Component
@Transactional
public class Bootstrap implements ApplicationRunner {


    private RoleRepository roleRepository;


    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public Bootstrap(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println(roleRepository.count());

        if(roleRepository.count()<1){
            Roles admin =new Roles();
            admin.setAuthority("ROLE_ADMIN");

            Roles customer = new Roles();
            customer.setAuthority("ROLE_CUSTOMER");

            Roles seller = new Roles();
            seller.setAuthority("ROLE_SELLER");

            roleRepository.save(admin);
            roleRepository.save(customer);
            roleRepository.save(seller);

        }
        if(userRepository.count()<1){

            List<Address> addresses = new ArrayList<>();
            Address address = new Address();
            address.setCity("Lucknow");
            address.setState("Uttar Pradesh");
            address.setCountry("India");
            address.setAddressLine("Hazratganj");
            address.setZipcode("263139");
            address.setLabel("home");

            User user = new User();
            user.setFirstName("Harsh");
            user.setLastName("Gupta");
            user.setIsActive(true);
            user.setEmail("harshgupta6201@gmail.com");
            user.setIsDeleted(false);
            user.setIsExpired(false);
            user.setIsLocked(false);
            user.setInvalidAttemptCount(0);
            user.setPassword(passwordEncoder.encode("Admin@123"));

            address.setUser(user);

            addresses.add(address);
            user.setAddresses(addresses);

            Roles roles = roleRepository.findByAuthority("ROLE_ADMIN").get();
            System.out.println(roles.getId());
            user.setRoles(Collections.singletonList(roles));

            userRepository.save(user);



        }
    }
}