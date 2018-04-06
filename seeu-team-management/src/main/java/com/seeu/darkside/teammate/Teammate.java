package com.seeu.darkside.teammate;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class Teammate {
    private Long idTeammate;

    public Long getIdTeammate() {
        return idTeammate;
    }

    public void setIdTeammate(Long idTeammate) {
        this.idTeammate = idTeammate;
    }
}
