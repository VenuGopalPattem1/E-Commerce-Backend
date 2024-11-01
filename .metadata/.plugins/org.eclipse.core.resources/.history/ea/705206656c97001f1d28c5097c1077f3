package com.nt.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nt.entity.Customer;

public interface IUserMngRepo extends JpaRepository<Customer, Integer> {
	public Customer findByEmailAndPassword(String email,String password);
	public Customer findByNameAndEmail(String name,String email);
	public Optional<Customer> findByEmail(String email);
}
