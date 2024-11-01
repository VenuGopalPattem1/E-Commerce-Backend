package com.nt.service;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nt.entity.Customer;
import com.nt.repo.IUserMngRepo;

@Service
public class AuthService {
	
	@Autowired
	private IUserMngRepo repo;
	
	   @Autowired
	  private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtUtils utils;
	
	public String saveUser(Customer user) {
		Optional<Customer> opt = repo.findByEmail(user.getEmail());
		if(opt.isEmpty()) {
			//converting user account data to the user master account data
			Customer master = new Customer();
			//copy the properties of user account data to the user Master data
			BeanUtils.copyProperties(user, master);
			master.setPassword(passwordEncoder.encode(user.getPassword()));
			//setting the ransom password to the master object
//			String tempPass = randomPassword(6);
//			master.setPassword(tempPass);
			//setting the user as the inactive user
//			master.setActive_sw("InActive");
			//saving the User Master Object into the DB
		   Customer um = 	repo.save(master);
			return um.getId()+" with the id value The User Object is Saved";
		}else {
			return "Customer is already present with given email";
		}
	}
   

    public String generateToken(String username) {
        return utils.generateToken(username);
    }

    public void validateToken(String token) {
    	utils.validateToken(token);
    }

}
