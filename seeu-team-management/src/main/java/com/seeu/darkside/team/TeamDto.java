package com.seeu.darkside.team;

import lombok.*;

import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TeamDto {

    private Long id;
    private String name;
    private String description;
    private String place;
    private String profilePhotoUrl;
    private Date created;
    private Date updated;

    @Override
    public String toString() {
        return "TeamDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", place='" + place + '\'' +
                ", created=" + created +
                ", updated=" + updated +
                '}';
    }
}