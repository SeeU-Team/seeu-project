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
}
