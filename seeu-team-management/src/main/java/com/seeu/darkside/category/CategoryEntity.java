package com.seeu.darkside.category;

import lombok.*;

import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CategoryEntity {

	private Long id;
    private String name;

    @Override
    public String toString() {
        return "CategoryEntity{" +
                ", name='" + name + '}';
    }
}
