package com.sunBank.security.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sunBank.exceptions.UserNotFoundException;
import com.sunBank.security.entities.AppRole;
import com.sunBank.security.entities.AppUser;
import com.sunBank.security.repositories.AppRoleRepository;
import com.sunBank.security.repositories.AppUserRepository;

@Service
@Transactional
public class AccountServiceImpl implements AccountService{
	
	@Autowired
	private AppUserRepository appUserRepository;
	
	@Autowired
	private AppRoleRepository appRoleRepository;
	
	private PasswordEncoder passwordEncoder;
	
	
	public AccountServiceImpl(AppUserRepository appUserRepository,AppRoleRepository appRoleRepository,PasswordEncoder passwordEncoder)
	{
		this.appRoleRepository = appRoleRepository;
		this.appUserRepository = appUserRepository;
		this.passwordEncoder = passwordEncoder;
	}
	// addNew User
	@Override
	public AppUser addNewUser(AppUser appUser) {
		String pw = appUser.getPassword();
		appUser.setPassword(passwordEncoder.encode(pw));  // encode the password here 
		return appUserRepository.save(appUser);
	}
	
	// delete user 
	@Override
	public void deleteUser(Long id) {
		appUserRepository.deleteById(id);
	}
	
	// add new role 
	@Override
	public AppRole addNewRole(AppRole appRole) {
		return appRoleRepository.save(appRole);
	}
	
	// add role to user 
	@Override
	public void addRoleToUser(String username, String rolename) {
		AppUser appUser = appUserRepository.findAppUserByUsername(username);
		AppRole appRole = appRoleRepository.findAppRoleByRoleName(rolename); 
		// role will be added to user 
		appUser.getRoles().add(appRole); 
	}
	
	//load user by user name 
	@Override
	public AppUser loadUserByUsername(String username) {
		return appUserRepository.findAppUserByUsername(username);
	}
	
	// list all users 
	@Override
	public List<AppUser> listUsers() {
		return appUserRepository.findAll();
	}
	
	// update user 
	@Override
	public AppUser update(AppUser appUser) {
//		String pw = appUser.getPassword();
//		appUser.setPassword(passwordEncoder.encode(pw));
//		return appUserRepository.;
		
		AppUser updateUser = appUserRepository.findById(appUser.getId()).orElse(null);
		
		updateUser.setUsername(appUser.getUsername());
		
		if(appUser.getPassword() != null && !appUser.getPassword().isEmpty())
		{
			updateUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
		}
		
		return appUserRepository.save(updateUser);
	}

}
