package com.seeu.darkside.utils;

import com.seeu.darkside.asset.TeamHasAssetRepository;
import com.seeu.darkside.category.TeamHasCategoryRepository;
import com.seeu.darkside.tag.TeamHasTagRepository;
import com.seeu.darkside.team.TeamRepository;
import com.seeu.darkside.teamup.MergeRepository;
import com.seeu.darkside.teamup.TeamUpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class CronService {

	private MergeRepository mergeRepository;
	private TeamHasTagRepository teamHasTagRepository;
	private TeamHasCategoryRepository teamHasCategoryRepository;
	private TeamHasAssetRepository teamHasAssetRepository;
	private TeamUpRepository teamUpRepository;
	private TeamRepository teamRepository;

	@Autowired
	public CronService(MergeRepository mergeRepository,
			TeamHasTagRepository teamHasTagRepository,
			TeamHasCategoryRepository teamHasCategoryRepository,
			TeamHasAssetRepository teamHasAssetRepository,
			TeamUpRepository teamUpRepository,
			TeamRepository teamRepository) {
		this.mergeRepository = mergeRepository;
		this.teamHasTagRepository = teamHasTagRepository;
		this.teamHasCategoryRepository = teamHasCategoryRepository;
		this.teamHasAssetRepository = teamHasAssetRepository;
		this.teamUpRepository = teamUpRepository;
		this.teamRepository = teamRepository;
	}

	@Scheduled(cron = "0 0 16 * * *")
	public void resetTables() {
		mergeRepository.deleteAll();
		teamHasTagRepository.deleteAll();
		teamHasAssetRepository.deleteAll();
		teamHasCategoryRepository.deleteAll();
		teamUpRepository.deleteAll();
	}
}
