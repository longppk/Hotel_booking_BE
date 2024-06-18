package com.hotel.booking.repository;

import com.hotel.booking.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);
    List<Users> findAllByOrderByIdDesc();
    @Query("SELECT u FROM Users u WHERE u.role <> 'ADMIN'")
    List<Users> findAllNonAdminUsers();

}
