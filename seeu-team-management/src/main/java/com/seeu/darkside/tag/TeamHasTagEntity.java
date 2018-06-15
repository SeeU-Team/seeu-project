package com.seeu.darkside.tag;

import lombok.*;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "team_has_tag")
public class TeamHasTagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_team_has_tag")
    private Long idTeamHasTag;

    @Column(name = "team_id")
    private Long teamId;

    @Column(name = "tag_id")
    private Long tagId;
}
