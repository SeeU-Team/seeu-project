package com.seeu.darkside.gateway.admin;

public interface AdminService {

	String authenticateAdmin(String email, String password);

	void addAdmin(String email, String password) throws AdminAlreadyExistsException;
}
