package com.seeu.darkside.teamup;

import lombok.*;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "teamup")
public class TeamUpEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_teamup")
    private Long idTeamUp;

    @Column(name = "id_like")
    private Long idLike;

    @Column(name = "id_liked")
    private Long idLiked;
}
