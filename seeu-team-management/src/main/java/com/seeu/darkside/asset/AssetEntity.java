package com.seeu.darkside.asset;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssetEntity {

    private Long idAsset;

    private String name;

    private String imageDark;

    private String imageLight;

    private Date created;

    private Date updated;

    public Long getIdAsset() {
        return idAsset;
    }

    public void setIdAsset(Long idAsset) {
        this.idAsset = idAsset;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageDark() {
        return imageDark;
    }

    public void setImageDark(String imageDark) {
        this.imageDark = imageDark;
    }

    public String getImageLight() {
        return imageLight;
    }

    public void setImageLight(String imageLight) {
        this.imageLight = imageLight;
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

    @Override
    public String toString() {
        return "AssetEntity{" +
                "idAsset=" + idAsset +
                ", name='" + name + '\'' +
                ", imageDark='" + imageDark + '\'' +
                ", imageLight='" + imageLight + '\'' +
                ", created=" + created +
                ", updated=" + updated +
                '}';
    }
}
