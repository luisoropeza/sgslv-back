package com.example.demo.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.request.TeamDto;
import com.example.demo.dtos.response.TeamResponse;
import com.example.demo.services.TeamService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class TeamController {

    private final TeamService teamService;

    @PostMapping("/teams")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<TeamResponse> createTeam(@RequestBody @Valid TeamDto dto) {
        TeamResponse team = teamService.createTeam(dto);
        return ResponseEntity.created(URI.create("/team/" + team.getId())).body(team);
    }

    @GetMapping("team/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<TeamResponse> getTeamById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(teamService.getTeamById(id));
    }

    @GetMapping("/teams")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<List<TeamResponse>> getAllTeams() {
        return ResponseEntity.ok(teamService.getAllTeams());
    }
}
