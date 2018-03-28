package com.seeu.darkside.user;

import lombok.Builder;

import java.util.Date;
import java.util.UUID;

@Builder
public class UserDto {

    private Long idUser;

    private String firstname;

    private String lastname;

    private String email;

    private String description;

    private String password;

    private String profilePhotoUrl;

    private Date created;

    private Date updated;

    public UserDto() {
    }

    public UserDto(Long idUser, String firstname, String lastname, String email, String description, String password, String profilePhotoUrl, Date created, Date updated) {
        this.idUser = idUser;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.description = description;
        this.password = password;
        this.profilePhotoUrl = profilePhotoUrl;
        this.created = created;
        this.updated = updated;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "idUser=" + idUser +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", description='" + description + '\'' +
                ", password='" + password + '\'' +
                ", profilePhotoUrl='" + profilePhotoUrl + '\'' +
                ", created=" + created +
                ", updated=" + updated +
                '}';
    }
}
