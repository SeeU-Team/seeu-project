package com.seeu.darkside.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "team_has_category")
public class TeamHasCategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_team_has_category")
    private Long idTeamHasCategory;

    @Column(name = "team_id")
    private Long teamId;

    @Column(name = "category_id")
    private Long categoryId;
}
