package com.seeu.darkside.tag;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TagDTO {
    private Long id;

    private String name;
}
