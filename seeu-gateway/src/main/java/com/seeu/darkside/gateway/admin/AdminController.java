package com.seeu.darkside.gateway.admin;

import com.seeu.darkside.gateway.authentication.admin.AdminLoginBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin")
public class AdminController {

	private final AdminService adminService;

	@Autowired
	public AdminController(AdminService adminService) {
		this.adminService = adminService;
	}

	@PostMapping
	public ResponseEntity addAdmin(@RequestBody @Valid AdminLoginBody adminBody) {
		try {
			adminService.addAdmin(adminBody.getEmail(), adminBody.getPassword());
		} catch (AdminAlreadyExistsException e) {
			return ResponseEntity.badRequest().build();
		}

		return new ResponseEntity(HttpStatus.CREATED);
	}
}
