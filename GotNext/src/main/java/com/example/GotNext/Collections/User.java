package com.example.GotNext.Collections;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document
public class User {
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    private String firstName;
    private String lastName;
    @Indexed(unique = true)
    private String email;
    @Indexed(unique = true)
    private String username;
    private String password;

    private ObjectId activeCourt;

    private List<ObjectId> usedCourts;

    private ObjectId activeTeam;

    private List<ObjectId> pastTeams;
    private LocalDateTime created;

    public User(String firstName, String lastName, String email, String username, String password, LocalDateTime created) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.activeCourt = null;
        this.usedCourts = null;
        this.activeTeam = null;
        this.pastTeams = null;
        this.created = created;
    }
}
