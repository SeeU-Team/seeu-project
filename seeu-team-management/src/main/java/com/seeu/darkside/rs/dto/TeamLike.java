package com.seeu.darkside.rs.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TeamLike {

    @NotNull
    private Long idLike;

    @NotNull
    private Long idLiked;
}
