package com.seeu.darkside.rs.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class TeamLike {

    private Long idLike;

    private Long idLiked;

    public Long getIdLike() {
        return idLike;
    }

    public void setIdLike(Long idLike) {
        this.idLike = idLike;
    }

    public Long getIdLiked() {
        return idLiked;
    }

    public void setIdLiked(Long idLiked) {
        this.idLiked = idLiked;
    }
}
