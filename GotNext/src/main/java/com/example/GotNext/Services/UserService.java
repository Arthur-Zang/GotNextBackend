package com.example.GotNext.Services;

import com.example.GotNext.Collections.User;
import com.example.GotNext.Repositories.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getUserById(ObjectId id) {
        return userRepository.findById(id).orElse(null);
    }

    public void update(User user) {
        userRepository.save(user);
    }
}