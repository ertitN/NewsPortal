package com.newportal.demo.dao;

import com.newportal.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDAO extends JpaRepository<User,Long> {

    User findByUserName(String userName);

    boolean existsByUserName(String userName);

    @Query(nativeQuery = true,
            value = "select password from users where user_name =?1")
    String getHashedPasswordByUserName(String userName);
}
