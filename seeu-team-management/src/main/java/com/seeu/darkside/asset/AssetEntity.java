package com.seeu.darkside.asset;

import lombok.*;

import java.util.Date;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AssetEntity {

    private Long id;

    private String name;

    private String imageDark;

    private String imageLight;

    private Date created;

    private Date updated;

    @Override
    public String toString() {
        return "AssetEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", imageDark='" + imageDark + '\'' +
                ", imageLight='" + imageLight + '\'' +
                ", created=" + created +
                ", updated=" + updated +
                '}';
    }
}
