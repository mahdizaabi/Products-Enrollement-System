package com.mahdi.microserviceusermanagement.service;

import com.mahdi.microserviceusermanagement.model.User;

import java.util.List;

public interface UserService {
    User save(User user);
    User findByUsername(String username);

    List<User> findUsers(List<Long> idList);
}
