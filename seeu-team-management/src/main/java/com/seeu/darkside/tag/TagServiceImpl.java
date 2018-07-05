package com.seeu.darkside.tag;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robin on 05/07/2018.
 */
@Service
public class TagServiceImpl implements TagService {

	private final TeamHasTagRepository teamHasTagRepository;
	private final TagServiceProxy tagServiceProxy;

	public TagServiceImpl(TeamHasTagRepository teamHasTagRepository, TagServiceProxy tagServiceProxy) {
		this.teamHasTagRepository = teamHasTagRepository;
		this.tagServiceProxy = tagServiceProxy;
	}

	@Override
	public void deleteAll(Long teamId) {
		List<TeamHasTagEntity> allByTeamId = teamHasTagRepository.findAllByTeamId(teamId);
		teamHasTagRepository.deleteAll(allByTeamId);
	}

	@Override
	public void saveAll(List<TeamHasTagEntity> teamHasTagToSave) {
		teamHasTagRepository.saveAll(teamHasTagToSave);
	}

	@Override
	public List<TagEntity> getTagsEntitiesFromIds(List<TeamHasTagEntity> tagEntitiesIds) {
		List<TagEntity> tagEntities = new ArrayList<>();
		for (TeamHasTagEntity tagEntity : tagEntitiesIds)
			tagEntities.add(tagServiceProxy.getTagInfo(tagEntity.getTagId()));
		return tagEntities;
	}

	@Override
	public List<TeamHasTagEntity> extractTags(List<Tag> tags, Long idTeam) {
		if (null == tags) {
			return new ArrayList<>();
		}

		List<TeamHasTagEntity> tagEntities = new ArrayList<>();
		for (Tag tag : tags) {
			TagEntity newTagIfNotExist = tagServiceProxy.createNewTagIfNotExist(new TagDTO(null, tag.getTagName()));
			TeamHasTagEntity teamHasTagEntity = TeamHasTagEntity.builder().teamId(idTeam).tagId(newTagIfNotExist.getIdTag()).build();
			tagEntities.add(teamHasTagEntity);
		}
		return tagEntities;
	}

	@Override
	public List<TeamHasTagEntity> findAllByTeamId(Long idTeam) {
		return teamHasTagRepository.findAllByTeamId(idTeam);
	}
}
