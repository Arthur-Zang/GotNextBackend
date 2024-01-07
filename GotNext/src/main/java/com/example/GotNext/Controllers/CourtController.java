package com.example.GotNext.Controllers;

import com.example.GotNext.Collections.Court;
import com.example.GotNext.Collections.Team;
import com.example.GotNext.Services.CourtService;
import com.example.GotNext.Services.TeamService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/courts")
public class CourtController {

    @Autowired
    private CourtService courtService;

    @Autowired
    private TeamService teamService;

    @GetMapping("/all")
    public ResponseEntity<List<Court>> getAllCourts() {
        //return the id as a tostring
        List<Court> allCourts = courtService.getAllCourts();
        return ResponseEntity.ok(allCourts);
    }

    @GetMapping("/{courtId}/teams")
    public ResponseEntity<List<Team>> getTeamsInCourt(String courtId) {
        ObjectId courtObjectId = new ObjectId(courtId);
        Optional<Court> court = courtService.getCourtById(courtId);
        List<Team> teamsInCourt = new ArrayList<>();
        court.ifPresent(x -> {
            List<ObjectId> teams = x.getTeams();
            for (int i = 0; i < teams.size(); i++) {
                Team temp = teamService.getTeamById(teams.get(i));
                teamsInCourt.add(temp);
            }
        });
        return ResponseEntity.ok(teamsInCourt);
    }
}