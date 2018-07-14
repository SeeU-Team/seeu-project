package com.seeu.darkside.gateway.admin;

import java.util.List;

public interface AdminService {

	List<AdminEntity> getAllAdmins();

	String authenticateAdmin(String email, String password);

	void addAdmin(String email, String password) throws AdminAlreadyExistsException;

	void deleteAdmin(Long id);
}
