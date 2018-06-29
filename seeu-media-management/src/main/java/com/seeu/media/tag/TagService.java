package com.seeu.media.tag;

import com.seeu.media.rs.dto.TagDto;

import java.util.List;

public interface TagService {
    TagEntity getTag(Long tagId);

    List<TagEntity> getAllTags();

    TagEntity createTag(TagDto tagDto);

    void updateTagName(Long tagId, String newName);

    void deleteTag(Long tagId);
}
