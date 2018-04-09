package com.seeu.darkside.teamup;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "teamup")
public class TeamUpEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_teamup")
    private Long idTeamUp;

    @Column(name = "id_like")
    private Long idLike;

    @Column(name = "id_liked")
    private Long idLiked;

    public Long getIdTeamUp() {
        return idTeamUp;
    }

    public void setIdTeamUp(Long idTeamUp) {
        this.idTeamUp = idTeamUp;
    }

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
