package com.seeu.media.rs;

import com.seeu.media.rs.dto.TagDto;
import com.seeu.media.rs.exception.TagNameIsNullException;
import com.seeu.media.tag.TagEntity;
import com.seeu.media.tag.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/medias/tags")
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public TagEntity createNewTagIfNotExist(@RequestBody @Valid TagDto tagDto) {

        if (tagDto.getName() == null) {
            throw new TagNameIsNullException("Tag name is null");
        }
        return tagService.createTagIfNotExist(tagDto);
    }

    @GetMapping("/{id}")
    public TagEntity getTagInfo(@PathVariable("id") Long tagId) {
        return tagService.getTag(tagId);
    }

    @PutMapping
    public ResponseEntity updateTagInfo(@RequestBody TagDto tag) {
        tagService.updateTagName(tag.getId(), tag.getName());
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteTag(@PathVariable("id") Long idTag) {
        tagService.deleteTag(idTag);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping
    public List<TagEntity> listTags() {
        return tagService.getAllTags();
    }
}
