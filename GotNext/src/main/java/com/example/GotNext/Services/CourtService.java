package com.example.GotNext.Services;

import com.example.GotNext.Collections.Court;
import com.example.GotNext.Repositories.CourtRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourtService {

    @Autowired
    private CourtRepository courtRepository;

    public void removeTeamFromCourts(ObjectId teamId) {
        List<Court> courts = courtRepository.findByTeamsContaining(teamId);
        for (Court court : courts) {
            court.getTeams().remove(teamId);
            courtRepository.save(court);
        }
    }
    public List<Court> getAllCourts() {
        return courtRepository.findAll();
    }
    public Optional<Court> getCourtById(String courtId) {
        return courtRepository.findById(courtId);
    }

    public void update(Court court) {
        courtRepository.save(court);
    }
}