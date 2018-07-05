package com.seeu.darkside.rs.dto;

import com.seeu.darkside.asset.Asset;
import com.seeu.darkside.category.Category;
import com.seeu.darkside.tag.Tag;
import com.seeu.darkside.user.Teammate;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TeamCreation {

    @NotEmpty
    private String name;

    private String description;
    private String place;

    private List<Teammate> members;
    private List<Asset> assets;
    private List<Category> categories;
    private List<Tag> tags;
}
