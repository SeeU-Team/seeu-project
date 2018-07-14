package com.seeu.darkside.gateway.admin;

import com.seeu.darkside.gateway.authentication.admin.AdminLoginBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

	private final AdminService adminService;

	@Autowired
	public AdminController(AdminService adminService) {
		this.adminService = adminService;
	}

	@GetMapping
	public List<AdminEntity> getAllAdmins() {
		return adminService.getAllAdmins();
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

	@DeleteMapping("/{id}")
	public void deleteAdmin(@PathVariable("id") Long id) {
		adminService.deleteAdmin(id);
	}
}
