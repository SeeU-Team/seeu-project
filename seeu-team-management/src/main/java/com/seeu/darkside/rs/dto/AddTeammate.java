package com.seeu.darkside.rs.dto;

import com.seeu.darkside.teammate.Teammate;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class AddTeammate {

    private Long idTeam;

    private List<Teammate> teammates;

    public Long getIdTeam() {
        return idTeam;
    }

    public void setIdTeam(Long idTeam) {
        this.idTeam = idTeam;
    }

    public List<Teammate> getTeammates() {
        return teammates;
    }

    public void setTeammates(List<Teammate> teammates) {
        this.teammates = teammates;
    }
}
