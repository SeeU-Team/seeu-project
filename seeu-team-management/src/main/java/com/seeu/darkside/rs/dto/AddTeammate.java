package com.seeu.darkside.rs.dto;

import com.seeu.darkside.teammate.Teammate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddTeammate {

    private Long idTeam;

    private List<Teammate> teammates;
}
