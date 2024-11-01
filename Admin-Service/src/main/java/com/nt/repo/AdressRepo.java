package com.nt.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nt.entity.Customer;

public interface AdressRepo extends JpaRepository<Customer, Long> {

}
