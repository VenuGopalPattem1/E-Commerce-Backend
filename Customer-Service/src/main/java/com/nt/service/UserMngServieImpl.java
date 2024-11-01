package com.nt.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nt.entity.Customer;
import com.nt.model.ActiveUser;
import com.nt.model.LoginCradentials;
import com.nt.model.RecoverPassword;
import com.nt.repo.IUserMngRepo;
import com.nt.util.EmailUtils;

@Service
public class UserMngServieImpl implements IUserMngService{
	
	@Autowired
	private IUserMngRepo repo;
	
	@Autowired
	private EmailUtils email;
	
	@Autowired
	private org.springframework.core.env.Environment env;
	
	@Override
	public String registerUser(Customer user) throws Exception {
		Optional<Customer> opt = Optional.ofNullable(repo.findByEmailAndPassword(user.getEmail(),user.getPassword()));
		if(opt.isEmpty()) {
			//converting user account data to the user master account data
			Customer master = new Customer();
			//copy the properties of user account data to the user Master data
			BeanUtils.copyProperties(user, master);
			//setting the ransom password to the master object
			String tempPass = randomPassword(6);
			master.setPassword(tempPass);
			//setting the user as the inactive user
//			master.setActive_sw("InActive");
			//saving the User Master Object into the DB
		   Customer um = 	repo.save(master);
		   
		   //TODO :: send the random password and some text to the user Email adress
		   //perform send mail operation
		   String subject = "User Registration Sucessfull";
		   String body  = readEmailMsg(env.getProperty("email.reg"),user.getName() , tempPass);
		   email.sendEmailMessage(user.getEmail(), subject, body);
		   //return the mag as the object is  saved
			return um.getId()+" with the id value The User Object is Saved";
		}else {
			return "Customer is already present with given email";
		}
	}

	@Override
	public String activateUser(ActiveUser active) {
		//find that the user object is avilable or not
		Customer master = repo.findByEmailAndPassword(active.getEmail(), active.getTempPass());
		if(master==null) {
			return "User Object is not found for the Activation";
		}else {
			//if user is avilable set password and set user status as active
			master.setPassword(active.getConfirmPass());
//			master.setActive_sw("Active");
			//save the user master object
		    repo.save(master);
			return "User Object is Found and Updated the Status as The active User";
		}
	}

	@Override
	public String login(LoginCradentials cred) {
	    // Find user by email
	    Optional<Customer> userOpt = repo.findByEmail(cred.getEmail());

	    // Check if the user exists
	    if (userOpt.isEmpty()) {
	        return "Invalid credentials: User not found";
	    }

	    Customer user = userOpt.get();

	    // Check if the password matches
	    if (!user.getPassword().equals(cred.getPassword())) {
	        return "Invalid credentials: Incorrect password";
	    }

	    // If valid credentials
	    return "Valid credentials: Login successful";
	}



	@Override
	public List<Customer> getAllUsers() {
		List<Customer> list = repo.findAll();
		List<Customer> account = new ArrayList<Customer>();
		list.forEach(info->{
			Customer user = new Customer();
			BeanUtils.copyProperties(info, user);
			account.add(user);
		});
		return account;
	}

	@Override
	public Customer getUserById(Integer id) {
		Customer user = repo.findById(id).orElseThrow(()-> new  IllegalArgumentException("User Object is Not found with id value "+id));
		Customer account =  new Customer();
		BeanUtils.copyProperties(user, account);
		return account;
	}

	@Override
	public Customer showUserByEmailAndName(String email, String name) {
		Customer master = repo.findByNameAndEmail(name,email);
		Customer account=null;
		if(master!=null) {
			account = new Customer();
			BeanUtils.copyProperties(master, account);
			return account;
		}
		return account;
	}

	@Override
	public String deleteUserById(Integer id) {
//		Optional<Customer> master = repo.findById(id);
//		if(master.isPresent()) {
//			Customer um =master.get();
//			repo.deleteById(um.getId());
//			return "User Object Is Found And deled With Id Value "+(um.getId());
//		}
		return "User Object is Not Found With Id Value"+id;
	}

	@Override
	public String updateUser(ActiveUser user) {
		Customer master=repo.findByEmailAndPassword(user.getEmail(),user.getTempPass());
		if(master!=null) {
			master.setPassword(user.getConfirmPass());
			repo.save(master);
			return " User Object is found and Password id Updated";
		}
		return "User Object is Not Found";
	}

	@Override
	public String updateActiveStatus(Integer id, String status) {
//		Customer master = repo.findById(id).orElseThrow(()-> new  IllegalArgumentException("User Object is Not found with id value "+id));
//		if(master!=null) {
//			master.setActive_sw(status);
//			repo.save(master);
//			return " User Object Is Found And Updated";
//		}
		return" User Object Is Not Found";
	}

	@Override
	public String recoverPassword(RecoverPassword recover) throws Exception {
		Customer master = repo.findByNameAndEmail(recover.getName(), recover.getEmail());
		if(master!=null) {
			String pass = master.getPassword();
			//TODO send this pass word to The User Email
			String subject = "mail for password recovery";
			String mailBody = readEmailMsg(env.getProperty("email.rec"), recover.getName(), pass);
			email.sendEmailMessage(recover.getEmail(), subject, mailBody);
			return pass;
		}
		return "Invalid UserName And Email";
	}
	
	private String randomPassword(int length) {
		//list of character to choose from to from an string
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"; 
		//creating a string buffer size of AlphaNumericString
		StringBuffer buffer = new  StringBuffer(length);
		for(int i=0;i<length;i++) {
			int ch = (int) (chars.length()*Math.random());
			buffer.append(chars.charAt(ch));
		}
		return buffer.toString();
	}
	
	
	//private Mthod to read mail msg body from file
	private String readEmailMsg(String fileName,String fullName,String pwd) throws Exception{
		String mailBody = null;
		String url =" ";
		try {
			FileReader read = new FileReader(fileName);
			BufferedReader br = new BufferedReader(read);
			//read file content to String buffer Object
			StringBuffer buff = new StringBuffer();
			String line =null;
			do {
				line=br.readLine();
				if(line!=null)
				buff.append(line);
			}while(line!=null);
			mailBody = buff.toString();
			mailBody = mailBody.replace("{FULL-NAME}", fullName);
			mailBody = mailBody.replace("{PWD}", pwd);
			mailBody = mailBody.replace("{URL}", url);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return mailBody;
	}
	
	

}
