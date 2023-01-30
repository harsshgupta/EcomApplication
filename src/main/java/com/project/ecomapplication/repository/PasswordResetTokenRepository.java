package com.project.ecomapplication.repository;

import com.project.ecomapplication.entities.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    @Query(value = "SELECT * FROM PasswordResetToken a WHERE a.user_id = ?1", nativeQuery = true)
    PasswordResetToken existsByUserId(Long id);

    @Query(value = "SELECT * FROM PasswordResetToken a WHERE a.token = ?1", nativeQuery = true)
    PasswordResetToken findByToken(String token);

    @Query(value = "SELECT a.user_id FROM PasswordResetToken a WHERE a.token = ?1", nativeQuery = true)
    Long findByUserId(String token);

}