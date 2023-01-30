package com.project.ecomapplication.repository;

import com.project.ecomapplication.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    User findUserByEmail(String email);

    @Query(value = "SELECT a.isActive from user a WHERE a.id = ?1", nativeQuery = true)
    Boolean isUserActive(Long id);

    @Transactional
    @Modifying
    @Query("UPDATE User a " +
            "SET a.isActive = FALSE WHERE a.email = ?1")
    int enableUser(String email);

    @Transactional
    @Modifying
    @Query("UPDATE User a " +
            "SET a.isActive = FALSE WHERE a.email = ?1")
    void disableUser(String email);

    @Transactional
    @Modifying
    @Query("UPDATE User a " +
            "SET a.invalidAttemptCount = ?1 WHERE a.email = ?2")
    void updateInvalidAttemptCount(Integer invalidAttemptCount, String email);


    @Query(value = "SELECT a.id, a.firstName, a.lastName, a.email, a.isActive " +
            "FROM user a " +
            "WHERE a.id IN (SELECT userID from user_role where roleID = 2)", nativeQuery = true)
    List<Object[]> printPartialDataForCustomers();

    @Query(value = "SELECT a.id, a.firstName, a.lastName, a.email, a.isActive, b.companyContact, b.companyName " +
            "FROM user a, seller b " +
            "WHERE a.id IN (SELECT userID from user_role where roleID = 3) "
            ,
            nativeQuery = true)
    List<Object[]> printPartialDataForSellers();

}
