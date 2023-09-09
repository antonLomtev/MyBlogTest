package com.example.myblogtest.repository;

import com.example.myblogtest.entity.User;
import com.example.myblogtest.entity.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {


//    @Query("SELECT u FROM User u where u.status2 = ?1")
//    List<User> getUsersForUserStatus2(Status status2);
//
//    @Transactional
//    @Modifying
//    @Query("UPDATE User u SET u.status2 = ?2 WHERE u.id = ?1")
//    void  updateStatusUserId(Long userId, Status status);
}
