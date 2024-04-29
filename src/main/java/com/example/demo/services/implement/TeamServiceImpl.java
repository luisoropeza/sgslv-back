package com.example.demo.services.implement;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dtos.request.TeamDto;
import com.example.demo.dtos.response.TeamResponse;
import com.example.demo.exceptions.AppException;
import com.example.demo.models.Team;
import com.example.demo.repositories.jpa.TeamRepository;
import com.example.demo.services.TeamService;
import com.example.demo.services.mapper.MainMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final MainMapper mainMapper;

    @Override
    public TeamResponse createTeam(TeamDto dto) {
        if (teamRepository.existsByName(dto.getName())) {
            throw new AppException("That's name is already exist", HttpStatus.BAD_REQUEST);
        }
        Team team = mainMapper.toTeam(dto);
        teamRepository.save(team);
        return mainMapper.toTeamResponse(team);
    }

    @Override
    public TeamResponse updateTeamById(Long id, TeamDto dto) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new AppException("Team can't be found", HttpStatus.NOT_FOUND));
        team = mainMapper.toTeam(team, dto);
        teamRepository.save(team);
        return mainMapper.toTeamResponse(team);
    }

    @Override
    public TeamResponse getTeamById(Long id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new AppException("Team can't be found", HttpStatus.NOT_FOUND));
        return mainMapper.toTeamResponse(team);
    }

    @Override
    public List<TeamResponse> getAllTeams() {
        return teamRepository.findAll().stream().map(mainMapper::toTeamResponse).toList();
    }
}
