package com.seeu.darkside.category;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("seeu-media-management")
public interface CategoryServiceProxy {

	@GetMapping("/medias/categories/{categoryId}")
	CategoryEntity getCategoryInfo(@PathVariable("categoryId") Long categoryId);
}
