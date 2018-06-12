package com.seeu.darkside.team;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "team")
public class TeamEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_team")
    private Long idTeam;

    @Column
    @NotEmpty
    private String name;

    @Column
    private String description;

    @Column
    private String place;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    @Override
    public String toString() {
        return "TeamEntity{" +
                "idTeam=" + idTeam +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", place='" + place + '\'' +
                ", created=" + created +
                ", updated=" + updated +
                '}';
    }
}