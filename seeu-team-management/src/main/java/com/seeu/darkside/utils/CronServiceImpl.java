package com.seeu.darkside.utils;

import com.seeu.darkside.asset.TeamHasAssetRepository;
import com.seeu.darkside.category.TeamHasCategoryRepository;
import com.seeu.darkside.tag.TeamHasTagRepository;
import com.seeu.darkside.team.TeamRepository;
import com.seeu.darkside.teamup.MergeRepository;
import com.seeu.darkside.teamup.TeamUpRepository;
import com.seeu.darkside.user.TeamHasUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class CronServiceImpl implements CronService {

	private final MergeRepository mergeRepository;
	private final TeamHasTagRepository teamHasTagRepository;
	private final TeamHasCategoryRepository teamHasCategoryRepository;
	private final TeamHasAssetRepository teamHasAssetRepository;
	private final TeamUpRepository teamUpRepository;
	private final TeamHasUserRepository teamHasUserRepository;
	private final TeamRepository teamRepository;

	@Autowired
	public CronServiceImpl(MergeRepository mergeRepository,
						   TeamHasTagRepository teamHasTagRepository,
						   TeamHasCategoryRepository teamHasCategoryRepository,
						   TeamHasAssetRepository teamHasAssetRepository,
						   TeamUpRepository teamUpRepository,
						   TeamHasUserRepository teamHasUserRepository,
						   TeamRepository teamRepository) {

		this.mergeRepository = mergeRepository;
		this.teamHasTagRepository = teamHasTagRepository;
		this.teamHasCategoryRepository = teamHasCategoryRepository;
		this.teamHasAssetRepository = teamHasAssetRepository;
		this.teamUpRepository = teamUpRepository;
		this.teamHasUserRepository = teamHasUserRepository;
		this.teamRepository = teamRepository;
	}

	@Scheduled(cron = "0 0 16 * * *")
	@Override
	public void resetTables() {
		mergeRepository.deleteAll();
		teamHasTagRepository.deleteAll();
		teamHasAssetRepository.deleteAll();
		teamHasCategoryRepository.deleteAll();
		teamHasUserRepository.deleteAll();
		teamUpRepository.deleteAll();
//		teamRepository.deleteAll();
	}
}
