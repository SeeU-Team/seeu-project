package com.seeu.darkside.rs;

import com.seeu.darkside.asset.Asset;
import com.seeu.darkside.category.Category;
import com.seeu.darkside.tag.Tag;
import com.seeu.darkside.teammate.Teammate;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class TeamCreation {

    private String name;
    private String description;
    private String place;
    private List<Teammate> teammateList;
    private List<Asset> assets;
    private List<Category> categories;
    private List<Tag> tags;

    public List<Teammate> getTeammateList() {
        return teammateList;
    }

    public void setTeammateList(List<Teammate> teammateList) {
        this.teammateList = teammateList;
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

    public List<Asset> getAssets() {
        return assets;
    }

    public void setAssets(List<Asset> assets) {
        this.assets = assets;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}
