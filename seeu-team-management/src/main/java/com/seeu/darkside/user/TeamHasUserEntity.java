package com.seeu.darkside.user;

import lombok.*;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "team_has_user")
public class TeamHasUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_team_has_user")
    private Long idTeamHasUser;

    @Column(name = "team_id")
    private Long teamId;

    @Column(name = "user_id")
    private Long userId;

    @Column
    private TeammateStatus status;

    @Override
    public boolean equals(Object obj) {
        boolean sameSame = false;
        if (obj != null && obj instanceof TeamHasUserEntity) {
            sameSame = this.userId == ((TeamHasUserEntity) obj).getUserId();
        }
        return sameSame;
    }
}
