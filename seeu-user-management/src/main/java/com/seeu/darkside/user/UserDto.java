package com.seeu.darkside.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;

    @NotEmpty
    private Long facebookId;

	@NotEmpty
    private String firstname;

	@NotEmpty
    private String lastname;

	@NotEmpty
	@Email
    private String email;

	@Length(min = 8)
    private String password;

	private String description;

    private String profilePhotoUrl;

    private Date created;

    private Date updated;

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
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
