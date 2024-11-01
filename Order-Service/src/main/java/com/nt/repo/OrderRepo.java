package com.nt.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nt.entity.Order;

public interface OrderRepo extends JpaRepository<Order, Long> {
	public List<Order> findByCustomerId(Long id);
}
