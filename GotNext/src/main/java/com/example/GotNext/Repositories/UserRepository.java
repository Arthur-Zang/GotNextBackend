package com.example.GotNext.Repositories;

import com.example.GotNext.Collections.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    User findUserByEmail(String email);
    User findByUsername(String username);

    Optional<User> findById(ObjectId id);
}
