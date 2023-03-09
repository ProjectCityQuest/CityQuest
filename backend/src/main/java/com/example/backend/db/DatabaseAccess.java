package com.example.backend.db;

import com.example.backend.entity.User;

import java.util.List;

public interface DatabaseAccess {
    public List<User> getAllUser();
    public User getUserById(int id);
    public User getUserByEmail(String email);
}
