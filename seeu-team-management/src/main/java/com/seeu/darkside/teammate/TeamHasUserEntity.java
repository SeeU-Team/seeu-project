package com.seeu.darkside.teammate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "team_has_user")
public class TeamHasUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_team_has_user")
    private Long idTeamHasUser;

    @Column(name = "team_id")
    private Long teamId;

    @Column(name = "user_id")
    private Long assetId;

    @Column
    private String status;

    public Long getIdTeamHasUser() {
        return idTeamHasUser;
    }

    public void setIdTeamHasUser(Long idTeamHasUser) {
        this.idTeamHasUser = idTeamHasUser;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public Long getAssetId() {
        return assetId;
    }

    public void setAssetId(Long assetId) {
        this.assetId = assetId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
