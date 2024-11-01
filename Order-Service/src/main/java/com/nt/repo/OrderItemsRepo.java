package com.nt.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nt.entity.OrderItems;

public interface OrderItemsRepo extends JpaRepository<OrderItems, Long> {
	
//	@Query(value = "SELECT * FROM order_items WHERE order_id = :orderId", nativeQuery = true)
	List<OrderItems> findByOrderId(Long orderId);


}
