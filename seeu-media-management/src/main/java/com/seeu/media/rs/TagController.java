package com.seeu.media.rs;

import com.seeu.media.rs.dto.TagDTO;
import com.seeu.media.rs.exception.TagNameIsNullException;
import com.seeu.media.tag.TagEntity;
import com.seeu.media.tag.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.BadAttributeValueExpException;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/medias/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public TagEntity createNewTagIfNotExist(@RequestBody TagDTO tagDTO) {

        if (tagDTO.getName() == null) {
            throw new TagNameIsNullException("Tag name is null");
        }
        return tagService.createTagIfNotExist(tagDTO);
    }

    @GetMapping
    public TagEntity getTagInfo(@RequestParam Long tagId) {
        return tagService.getTag(tagId);
    }

    @PutMapping
    public ResponseEntity updateTagInfo(@RequestBody TagDTO tag) {
        tagService.updateTagName(tag.getIdTag(), tag.getName());
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity deleteTag(@RequestParam Long idTag) {
        tagService.deleteTag(idTag);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * DEBUG
     * @return
     */
    @GetMapping("/list")
    public List<TagEntity> listTags() {
        return tagService.getAllTags();
    }
}
