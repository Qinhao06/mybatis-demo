package com.example.mapper;

import com.example.model.User;
import org.example.build.Param;

import java.util.List;

public interface UserMapper {
    List<User> selectAllUsers();
    User selectUserById(@Param("id") int id);
    void insertUser(@Param("user") User user);
    void updateUser(@Param("user") User user);
    void deleteUser(@Param("id") int id);
}
