package com.example.demo.services;

import java.util.List;

import com.example.demo.dtos.request.TeamDto;
import com.example.demo.dtos.response.TeamResponse;

public interface TeamService {
    TeamResponse createTeam(TeamDto dto);

    TeamResponse updateTeamById(Long id, TeamDto dto);

    TeamResponse getTeamById(Long id);

    List<TeamResponse> getAllTeams();
}
