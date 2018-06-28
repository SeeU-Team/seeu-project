package com.seeu.darkside.team;

import com.seeu.darkside.rs.dto.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TeamService {
    List<TeamDto> getAllTeams();

    @Transactional
    TeamProfile createTeam(TeamCreation teamCreation, String imageBase64, String fileName);

    void checkIfTeamExist(Long idTeam) throws TeamNotFoundException;

    TeamProfile getTeamProfile(Long idTeam);

    TeamProfile addTeammates(AddTeammate teammates);

    TeamHasUser getTeamProfileOfMember(Long memberId);
}
