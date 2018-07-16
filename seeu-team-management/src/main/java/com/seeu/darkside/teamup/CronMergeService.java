package com.seeu.darkside.teamup;

import com.seeu.darkside.asset.TeamHasAssetRepository;
import com.seeu.darkside.category.TeamHasCategoryRepository;
import com.seeu.darkside.tag.TeamHasTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class CronMergeService {

	private MergeRepository mergeRepository;
	private TeamHasTagRepository teamHasTagRepository;
	private TeamHasCategoryRepository teamHasCategoryRepository;
	private TeamHasAssetRepository teamHasAssetRepository;
	private TeamUpRepository teamUpRepository;

	@Autowired
	public CronMergeService(MergeRepository mergeRepository,
			TeamHasTagRepository teamHasTagRepository,
			TeamHasCategoryRepository teamHasCategoryRepository,
			TeamHasAssetRepository teamHasAssetRepository,
			TeamUpRepository teamUpRepository) {
		this.mergeRepository = mergeRepository;
		this.teamHasTagRepository = teamHasTagRepository;
		this.teamHasCategoryRepository = teamHasCategoryRepository;
		this.teamHasAssetRepository = teamHasAssetRepository;
		this.teamUpRepository = teamUpRepository;
	}

	@Scheduled(cron = "0 0 16 * * *")
	public void pureMergeTable() {
		mergeRepository.deleteAll();
		teamHasTagRepository.deleteAll();
		teamHasAssetRepository.deleteAll();
		teamHasCategoryRepository.deleteAll();
		teamUpRepository.deleteAll();
	}
}
