package com.seeu.media.tag;

import com.seeu.media.rs.dto.TagDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    private TagRepository tagRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public TagEntity getTag(Long tagId) {
        return tagRepository.findTagEntityByIdTag(tagId);
    }

    @Override
    public List<TagEntity> getAllTags() {
        return tagRepository.findAll();
    }

    @Override
    public TagEntity createTag(TagDTO tagDTO) {

        Date now = new Date();

        TagEntity entityToCreate = TagEntity.builder()
                .name(tagDTO.getName())
                .created(now)
                .updated(now)
                .build();

        return tagRepository.save(entityToCreate);
    }
}
