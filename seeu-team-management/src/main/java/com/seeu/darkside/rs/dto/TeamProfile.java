package com.seeu.darkside.rs.dto;

import com.seeu.darkside.asset.TeamHasAssetEntity;
import com.seeu.darkside.category.TeamHasCategoryEntity;
import com.seeu.darkside.tag.TeamHasTagEntity;
import com.seeu.darkside.teammate.TeamHasUserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamProfile {

    private Long idTeam;
    private String name;
    private String description;
    private String place;
    private Date created;
    private Date updated;
    private List<TeamHasUserEntity> teammateList;
    private List<TeamHasAssetEntity> assets;
    private List<TeamHasCategoryEntity> categories;
    private List<TeamHasTagEntity> tags;

    public Long getIdTeam() {
        return idTeam;
    }

    public void setIdTeam(Long idTeam) {
        this.idTeam = idTeam;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public List<TeamHasAssetEntity> getAssets() {
        return assets;
    }

    public void setAssets(List<TeamHasAssetEntity> assets) {
        this.assets = assets;
    }

    public List<TeamHasCategoryEntity> getCategories() {
        return categories;
    }

    public void setCategories(List<TeamHasCategoryEntity> categories) {
        this.categories = categories;
    }

    public List<TeamHasTagEntity> getTags() {
        return tags;
    }

    public void setTags(List<TeamHasTagEntity> tags) {
        this.tags = tags;
    }

    public List<TeamHasUserEntity> getTeammateList() {
        return teammateList;
    }

    public void setTeammateList(List<TeamHasUserEntity> teammateList) {
        this.teammateList = teammateList;
    }
}
