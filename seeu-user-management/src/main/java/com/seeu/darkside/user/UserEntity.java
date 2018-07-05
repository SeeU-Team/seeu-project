package com.seeu.darkside.user;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "account")
public class UserEntity {

    @Id
    @GeneratedValue
    @Column(name = "id_user")
    private Long id;

    @Column
	private Long facebookId;

    @Column(name = "user_name")
    private String name;

    @Column
	private Gender gender;

    @Column
    private String email;

    @Column
    private String description;

    @Column(name = "user_photo_url")
    private String profilePhotoUrl;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", description='" + description + '\'' +
                ", profilePhotoUrl='" + profilePhotoUrl + '\'' +
                ", created=" + created +
                ", updated=" + updated +
                '}';
    }
}