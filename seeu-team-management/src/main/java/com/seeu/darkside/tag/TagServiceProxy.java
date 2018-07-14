package com.seeu.darkside.tag;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient("seeu-media-management")
public interface TagServiceProxy {

	@GetMapping("/medias/tags/{id}")
	TagEntity getTagInfo(@PathVariable("id") Long tagId);

	@PostMapping(value = "/medias/tags", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	TagEntity createNewTagIfNotExist(@RequestBody TagDTO tagDTO);
}
