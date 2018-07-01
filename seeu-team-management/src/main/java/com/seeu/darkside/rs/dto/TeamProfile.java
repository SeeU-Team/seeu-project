package com.seeu.darkside.rs.dto;

import com.seeu.darkside.asset.TeamHasAssetEntity;
import com.seeu.darkside.category.TeamHasCategoryEntity;
import com.seeu.darkside.tag.TeamHasTagEntity;
import com.seeu.darkside.teammate.TeamHasUserEntity;
import lombok.*;

import java.util.Date;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TeamProfile {

    private Long id;
    private String name;
    private String description;
    private String place;
    private String profilePhotoUrl;
    private Date created;
    private Date updated;

    // TODO: Remplacer les listes de XxxHasXxxEntity par des listes d'Entity en appelant le bon micro service pour chacun
    private List<TeamHasUserEntity> members;
    private List<TeamHasAssetEntity> assets;
    private List<TeamHasCategoryEntity> categories;
    private List<TeamHasTagEntity> tags;
}
