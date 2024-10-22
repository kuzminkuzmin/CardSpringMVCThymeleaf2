package com.dmitriikuzmin.service;

import com.dmitriikuzmin.model.User;
import com.dmitriikuzmin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public void addUser(String login, String password, String name) {
        try {
            this.userRepository.save(new User(login, password, name));
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Username is not available");
        }
    }

    @Override
    public User get(long id) {
        return this.userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + id + " not found"));
    }

    @Override
    public User get(String login, String password) {
        return this.userRepository.findByLoginAndPassword(login, password)
                .orElseThrow(() -> new IllegalArgumentException("Wrong Login or Password"));
    }

    @Override
    public User delete(long id) {
        User user = this.get(id);
        this.userRepository.deleteById(id);
        return user;
    }
}
