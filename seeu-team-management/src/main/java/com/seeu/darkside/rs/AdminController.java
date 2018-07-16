package com.seeu.darkside.rs;

import com.seeu.darkside.utils.CronService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Robin on 16/07/2018.
 */
@RestController
@RequestMapping("/admin/reset")
public class AdminController {

	@Autowired
	private CronService cronService;

	@DeleteMapping
	public void resetDataBase() {
		cronService.resetTables();
	}
}
