package com.project.ecomapplication.repository;

import com.project.ecomapplication.entities.AccessToken;
import com.project.ecomapplication.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface AccessTokenRepository extends JpaRepository<AccessToken, Long> {

    Optional<AccessToken> findByToken(String token);
    @Modifying
    @Transactional
    void deleteByUser(User user);

}