package com.project.ecomapplication.repository;
import com.project.ecomapplication.entities.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Long> {
  Optional<Roles> findByAuthority(String authority);
}