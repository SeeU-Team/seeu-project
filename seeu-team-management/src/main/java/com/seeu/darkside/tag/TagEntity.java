package com.seeu.darkside.tag;

import lombok.*;

import java.util.Date;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TagEntity {

    private Long id;
    private String name;
    private Date created;
    private Date updated;

    @Override
    public String toString() {
        return "TagEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", created=" + created +
                ", updated=" + updated +
                '}';
    }
}
