package com.example.GotNext.Controllers;

import com.example.GotNext.Collections.Court;
import com.example.GotNext.Collections.Team;
import com.example.GotNext.Collections.User;
import com.example.GotNext.Requests.CreateTeamRequest;
import com.example.GotNext.Requests.TeamRole;
import com.example.GotNext.Requests.TeamWithRoleResponse;
import com.example.GotNext.Requests.addPlayerBody;
import com.example.GotNext.Services.CourtService;
import com.example.GotNext.Services.TeamService;
import com.example.GotNext.Services.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @Autowired
    private UserService userService;

    @Autowired
    private CourtService courtService;

    @PostMapping("/create")
    public ResponseEntity<Team> createTeam(@RequestBody CreateTeamRequest createTeamRequest) {
        // Get the leaderId from the request
        String leaderId = createTeamRequest.getLeaderId();
        String courtId = createTeamRequest.getCourt();

        Optional<Court> court = courtService.getCourtById(courtId);

        // Check if the leader exists
        User leader = userService.getUserById(new ObjectId(leaderId));
        if (leader == null) {
            return ResponseEntity.badRequest().body(null); // Handle leader not found
        } else if (leader.getActiveTeam() != null){
            System.out.println("nice!");
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,"A new user cannot already be part of a team");
        }

        // Create a new team
        Team newTeam = new Team();
        newTeam.setLeader(leader.getId());
        newTeam.setMembers(new ArrayList<>()); // Initialize members list
        newTeam.getMembers().add(leader.getId()); // Add leader to members
        newTeam.setName(createTeamRequest.getTeamName());
        newTeam.setSize(createTeamRequest.getSize());


        // Save the new team
        Team createdTeam = teamService.createTeam(newTeam);
        //Set active team of leader
        leader.setActiveTeam(createdTeam.getId());
        userService.update(leader);
        //Add team to court
        List<ObjectId> tempTeams = court.get().getTeams();
        tempTeams.add(createdTeam.getId());
        courtService.update(court.get());
        return ResponseEntity.ok(createdTeam);
    }

    @PostMapping("/{teamId}/addPlayer")
    public ResponseEntity<Team> addPlayerToTeam(@PathVariable String teamId, @RequestBody addPlayerBody addPlayer) {
        Team team = teamService.getTeamById(new ObjectId(teamId));
        String playerId = addPlayer.getId();
        User player = userService.getUserById(new ObjectId(playerId));
        if (team == null || player == null) {
            return ResponseEntity.notFound().build(); // Handle team or player not found
        }
        if (player.getActiveTeam() != null) {
            addPlayerBody temp = new addPlayerBody();
            temp.setId(playerId);
            removePlayerFromTeam(player.getActiveTeam().toString(), temp);
        }
        // Check if the player is already a member of the team
        if (!team.getMembers().contains(player.getId())) {
            team.getMembers().add(player.getId());
            team.setSize(team.getSize() + 1);
            teamService.updateTeam(team);
            player.setActiveTeam(team.getId());
            userService.update(player);
        }
        return ResponseEntity.ok(team);
    }

    @PostMapping("/{teamId}/removePlayer")
    //If team leader, make next in time team leader
    //If team size 0, erase team
    public ResponseEntity<Team> removePlayerFromTeam(@PathVariable String teamId, @RequestBody addPlayerBody removePlayer) {
        Team team = teamService.getTeamById(new ObjectId(teamId));
        String playerId = removePlayer.getId();
        User player = userService.getUserById(new ObjectId(playerId));

        if (team == null || player == null) {
            return ResponseEntity.notFound().build(); // Handle team or player not found
        }
        if (team.getMembers().contains(player.getId())) {
            if (team.getMembers().size() == 1) {
                deleteTeam(new ObjectId(teamId));
            } else {
                if (playerId.equals(team.getLeader().toString())) {
                    for (int i = 0; i < team.getMembers().size(); i++) {
                        if (team.getMembers().get(i) != null && !team.getMembers().get(i).toString().equals(playerId)) {
                            team.setLeader(team.getMembers().get(i));
                        }
                    }
                }
                team.getMembers().remove(player.getId());
                team.setSize(team.getSize() - 1);
                teamService.updateTeam(team);
                player.setActiveTeam(null);
                userService.update(player);
            }


        }
        return ResponseEntity.ok(team);
    }

    @DeleteMapping("/{teamId}")
    public ResponseEntity<Void> deleteTeam(@PathVariable ObjectId teamId) {
        Team team = teamService.getTeamById(teamId);

        if (team == null) {
            return ResponseEntity.notFound().build(); // Handle team not found
        }
        for (int i = 0; i < team.getMembers().size(); i++) {
            ObjectId playerId = team.getMembers().get(i);
            User player = userService.getUserById(playerId);
            player.setActiveTeam(null);
            userService.update(player);
        }

        // Remove the team from any associated courts
        courtService.removeTeamFromCourts(team.getId());

        // Delete the team
        teamService.deleteTeam(team.getId());

        return ResponseEntity.noContent().build();
    }


    @GetMapping("/all")
    public ResponseEntity<List<TeamWithRoleResponse>> getTeamsInCourt(@RequestParam String playerId, @RequestParam String courtId) {
        System.out.println("doing" + playerId + courtId);
        Optional<Court> court = courtService.getCourtById(courtId);
        User player = userService.getUserById(new ObjectId(playerId));
        if (court.isPresent()) {
            List<ObjectId> teamIds = court.get().getTeams();
            List<Team> teamsInCourt = teamIds.stream()
                    .map(teamId -> teamService.getTeamById(teamId))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            List<TeamWithRoleResponse> teamsWithRole = teamsInCourt.stream()
                    .map(team -> {
                        TeamWithRoleResponse response = new TeamWithRoleResponse();
                        response.setTeam(team);

                        if (team.getLeader().equals(player.getId())) {
                            response.setRole(TeamRole.LEADER);
                        } else if (team.getMembers().contains(player.getId())) {
                            response.setRole(TeamRole.MEMBER);
                        } else {
                            response.setRole(TeamRole.NONE);
                        }
                        User leader = userService.getUserById(team.getLeader());
                        response.setId(team.getId().toString());
                        response.setLeaderId(leader.getId().toString());
                        response.setLeaderName(leader.getUsername());
                        response.setSize(team.getSize());
                        response.setTime(team.getCreated());
                        response.setTitle(team.getName());
                        return response;
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.ok(teamsWithRole);
        } else {
            return ResponseEntity.notFound().build(); // Handle court not found
        }
    }


}
