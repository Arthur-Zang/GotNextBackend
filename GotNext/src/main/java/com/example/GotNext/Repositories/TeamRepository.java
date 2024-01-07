package com.example.GotNext.Repositories;

import com.example.GotNext.Collections.Team;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TeamRepository extends MongoRepository<Team, ObjectId> {
    // Additional custom queries or methods can be defined here if needed
}