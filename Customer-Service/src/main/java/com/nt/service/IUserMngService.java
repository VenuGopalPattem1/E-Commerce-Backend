package com.nt.service;

import java.util.List;

import com.nt.entity.Customer;
import com.nt.model.ActiveUser;
import com.nt.model.LoginCradentials;
import com.nt.model.RecoverPassword;

public interface IUserMngService {
	
	public String registerUser(Customer user) throws Exception ;
	public String activateUser(ActiveUser active);
	public String login(LoginCradentials cred);
	public List<Customer> getAllUsers();
	public Customer getUserById(Integer id);
	public Customer showUserByEmailAndName(String email,String name);
	public String deleteUserById(Integer id);
	public String updateUser(ActiveUser  user);
	public String updateActiveStatus(Integer id,String status);
	public String recoverPassword(RecoverPassword recover) throws Exception;
}
