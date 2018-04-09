package com.seeu.darkside.asset;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "team_has_asset")
public class TeamHasAssetEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_team_has_asset")
    private Long idTeamHasAsset;

    @Column(name = "team_id")
    private Long teamId;

    @Column(name = "asset_id")
    private Long assetId;

    @Column(name = "asset_media_id")
    private Long assetMediaId;

    public Long getIdTeamHasAsset() {
        return idTeamHasAsset;
    }

    public void setIdTeamHasAsset(Long idTeamHasAsset) {
        this.idTeamHasAsset = idTeamHasAsset;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public Long getAssetId() {
        return assetId;
    }

    public void setAssetId(Long assetId) {
        this.assetId = assetId;
    }

    public Long getAssetMediaId() {
        return assetMediaId;
    }

    public void setAssetMediaId(Long assetMediaId) {
        this.assetMediaId = assetMediaId;
    }
}
