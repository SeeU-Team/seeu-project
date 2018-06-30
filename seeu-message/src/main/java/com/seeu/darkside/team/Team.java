package com.seeu.darkside.team;

import lombok.*;

import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Team {

    private Long id;
    private String name;
    private String description;
    private String place;
    private Date created;
    private Date updated;
}
