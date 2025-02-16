package com.nt.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nt.entity.Customer;

public interface CustomerRepo extends JpaRepository<Customer, Long> {
	
	public Customer findByEmail(String email);
}
