package com.seeu.darkside.teamup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class CronMergeService {

	private MergeRepository mergeRepository;

	@Autowired
	public CronMergeService(MergeRepository mergeRepository) {
		this.mergeRepository = mergeRepository;
	}

	@Scheduled(cron = "0 0 16 * * *")
	public void pureMergeTable() {
		mergeRepository.deleteAll();
	}
}
