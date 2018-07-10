package com.seeu.darkside.rs.dto;

import com.seeu.darkside.asset.AssetEntity;
import com.seeu.darkside.category.CategoryEntity;
import com.seeu.darkside.tag.TagEntity;
import com.seeu.darkside.user.UserEntity;
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
    private List<UserEntity> members;
    private List<AssetEntity> assets;
    private List<CategoryEntity> categories;
    private List<TagEntity> tags;

    private boolean merged;
}
