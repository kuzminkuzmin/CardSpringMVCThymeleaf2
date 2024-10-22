package com.dmitriikuzmin.service;

import com.dmitriikuzmin.model.User;

public interface UserService {
    void addUser(String login, String password, String name);
    User get(long id);
    User get(String login, String password);
    User delete(long id);
}
