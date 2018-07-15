package com.seeu.media.asset;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "asset")
public class AssetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asset")
    private Long id;

    @Column
    private String name;

    @Column(name = "image_dark")
    private String imageDark;

    @Column(name = "image_light")
    private String imageLight;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    @Override
    public String toString() {
        return "AssetEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", imageDark='" + imageDark + '\'' +
                ", imageLight='" + imageLight + '\'' +
                ", created=" + created +
                ", updated=" + updated +
                '}';
    }
}
