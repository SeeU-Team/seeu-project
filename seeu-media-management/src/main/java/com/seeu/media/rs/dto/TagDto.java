package com.seeu.media.rs.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TagDto {

    private Long id;

    @NotNull
    @NotEmpty
    private String name;
}
