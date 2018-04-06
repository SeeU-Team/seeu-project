package com.seeu.darkside.tag;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "team_has_tag")
public class TeamHasTagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_team_has_tag")
    private Long idTeamHasTag;

    @Column(name = "team_id")
    private Long teamId;

    @Column(name = "tag_id")
    private Long tagId;

    public Long getIdTeamHasTag() {
        return idTeamHasTag;
    }

    public void setIdTeamHasTag(Long idTeamHasTag) {
        this.idTeamHasTag = idTeamHasTag;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }
}
