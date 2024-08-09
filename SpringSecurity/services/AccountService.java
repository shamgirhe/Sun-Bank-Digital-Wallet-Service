package com.sunBank.security.services;

import java.util.List;

import com.sunBank.security.entities.AppRole;
import com.sunBank.security.entities.AppUser;

public interface AccountService {
	AppUser addNewUser(AppUser appUser);
	
	void deleteUser(Long id);
	
	AppRole addNewRole(AppRole appRole);
	
	void addRoleToUser(String username,String rolename);
	
	AppUser loadUserByUsername(String username);
	
	List<AppUser> listUsers();
	
	AppUser update(AppUser appUser);
}
