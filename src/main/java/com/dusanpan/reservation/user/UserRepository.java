package com.dusanpan.reservation.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    @Transactional
    @Modifying
    @Query("UPDATE User u " +
            "SET u.enabled = true WHERE u.email = :email")
    int enableUser(@Param("email") String email);


    User findByUsernameOrEmail(String username, String email);
}
