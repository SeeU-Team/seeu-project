package com.seeu.darkside.tag;

import com.seeu.darkside.rs.dto.TeamCreation;

import java.util.List;

/**
 * Created by Robin on 05/07/2018.
 */
public interface TagService {
	void saveAll(List<TeamHasTagEntity> teamHasTagToSave);

	List<TagEntity> getTagsEntitiesFromIds(List<TeamHasTagEntity> tagEntitiesIds);

	List<TeamHasTagEntity> extractTags(TeamCreation teamCreation, Long idTeam);

	List<TeamHasTagEntity> findAllByTeamId(Long idTeam);
}
