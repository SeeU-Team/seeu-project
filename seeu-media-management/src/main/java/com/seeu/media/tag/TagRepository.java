package com.seeu.media.tag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, Long> {
    TagEntity findTagEntityByIdTag(Long idTag);

    @Override
    List<TagEntity> findAll();
}
