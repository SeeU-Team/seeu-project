package com.seeu.media.rs;

import com.seeu.media.rs.dto.TagDTO;
import com.seeu.media.tag.TagEntity;
import com.seeu.media.tag.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public TagEntity createNewTag(@RequestBody TagDTO tagDTO) {
        return tagService.createTag(tagDTO);
    }

    @GetMapping
    public TagEntity getTagInfo(@RequestParam Long tagId) {
        return tagService.getTag(tagId);
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
