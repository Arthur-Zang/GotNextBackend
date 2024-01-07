package com.example.GotNext.Services;

import com.example.GotNext.Collections.Team;
import com.example.GotNext.Repositories.TeamRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    public Team createTeam(Team team) {
        return teamRepository.save(team);
    }

    public Team getTeamById(ObjectId id) {
        return teamRepository.findById(id).orElse(null);
    }

    public void updateTeam(Team team) {
        teamRepository.save(team);
    }

    public void deleteTeam(ObjectId id) {
        teamRepository.deleteById(id);
    }

    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }
}