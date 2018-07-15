package com.seeu.darkside.asset;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssetDto {

	private Long id;
    private String name;
    private String imageDark;
    private String imageLight;
}
