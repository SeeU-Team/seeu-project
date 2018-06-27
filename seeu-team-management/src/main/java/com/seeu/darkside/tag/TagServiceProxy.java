package com.seeu.darkside.tag;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("seeu-media-management")
public interface TagServiceProxy {

	@GetMapping("/medias/tags")
	TagEntity getTagInfo(@RequestParam("tagId") Long tagId);
}
