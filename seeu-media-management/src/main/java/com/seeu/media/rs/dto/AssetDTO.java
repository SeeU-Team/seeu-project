package com.seeu.media.rs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetDTO {

    private Long idAsset;

    private String name;

    private int mediaId;

    public void setIdAsset(Long idAsset) {
        this.idAsset = idAsset;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMediaId() {
        return mediaId;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }
}
