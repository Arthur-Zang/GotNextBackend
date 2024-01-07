package com.example.GotNext.Repositories;

import com.example.GotNext.Collections.Court;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CourtRepository extends MongoRepository<Court, ObjectId> {

    List<Court> findByTeamsContaining(ObjectId teamId);
    Optional<Court> findById(String id);
}
