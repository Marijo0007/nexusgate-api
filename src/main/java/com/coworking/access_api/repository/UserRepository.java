package com.coworking.access_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.coworking.access_api.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
    List<User> findByActiveTrue();
    List<User> findByMembership(User.MembershipType membership);
    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE " + "LOWER(u.fullName) LIKe LOWER(CONCAT('%', :name, '%'))")
    List<User> findByFullNameContainingIgnoreCase(@Param("name") String name);
}