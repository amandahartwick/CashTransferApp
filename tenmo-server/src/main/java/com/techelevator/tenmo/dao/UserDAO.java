package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;

import java.util.List;

public interface UserDAO {

    List<User> findAll();

    User findByUsername(String username);

    int findIdByUsername(String username);
    
    String findUsernameByUserId(int user_id);

    boolean create(String username, String password);
    
	public User findByUserId(int user_id);

    
    // may not be needed ...
    // void printAll(List<User> findAll);
}
