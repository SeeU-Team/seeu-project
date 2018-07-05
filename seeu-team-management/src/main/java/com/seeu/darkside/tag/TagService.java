package com.seeu.darkside.tag;

import java.util.List;

/**
 * Created by Robin on 05/07/2018.
 */
public interface TagService {

	void deleteAll(Long teamId);

	void saveAll(List<TeamHasTagEntity> teamHasTagToSave);

	List<TagEntity> getTagsEntitiesFromIds(List<TeamHasTagEntity> tagEntitiesIds);

	List<TeamHasTagEntity> extractTags(List<Tag> tags, Long idTeam);

	List<TeamHasTagEntity> findAllByTeamId(Long idTeam);
}
