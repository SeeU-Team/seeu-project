package com.seeu.darkside.rs.dto;

import com.seeu.darkside.asset.TeamHasAssetEntity;
import com.seeu.darkside.category.TeamHasCategoryEntity;
import com.seeu.darkside.tag.TeamHasTagEntity;
import com.seeu.darkside.teammate.TeamHasUserEntity;
import com.seeu.darkside.user.User;
import lombok.*;

import java.util.Date;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TeamProfile {

    private Long idTeam;
    private String name;
    private String description;
    private String place;
    private Date created;
    private Date updated;
    private List<User> teammateList;
    private List<TeamHasAssetEntity> assets;
    private List<TeamHasCategoryEntity> categories;
    private List<TeamHasTagEntity> tags;
}
