package com.seeu.darkside.rs.dto;

import com.seeu.darkside.asset.AssetEntity;
import com.seeu.darkside.asset.TeamHasAssetEntity;
import com.seeu.darkside.category.CategoryEntity;
import com.seeu.darkside.category.TeamHasCategoryEntity;
import com.seeu.darkside.tag.TagEntity;
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
    private List<TeamHasUserEntity> members;
    private List<AssetEntity> assets;
    private List<CategoryEntity> categories;
    private List<TagEntity> tags;
}
