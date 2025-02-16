package com.nt.service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.dto.AddressDTO;
import com.nt.dto.CustomerDTO;
import com.nt.dto.OrderDTO;
import com.nt.dto.OrderItemsDTO;
import com.nt.entity.Address;
import com.nt.entity.Customer;
import com.nt.entity.Order;
import com.nt.entity.OrderItems;
import com.nt.repo.AdressRepo;
import com.nt.repo.CustomerRepo;
import com.nt.repo.OrderItemsRepo;
import com.nt.repo.OrderRepo;
@Service
public class OrderServiceImpl implements IOrderService {
	
	@Autowired
	private OrderRepo orderRepo;
	
	@Autowired
	private CustomerRepo customerRepo;
	
	@Autowired
	private AdressRepo addrepo;

	@Autowired
	private OrderItemsRepo orderItemsRepo;

	public String saveOrder(OrderDTO orderDTO) {
		// Step 1: Check if the customer already exists
		Customer existingCustomer = customerRepo.findByEmail(orderDTO.getCustomer().getEmail());
		Customer customer;

		if (existingCustomer != null) {
		    // If customer exists, use the existing record
		    customer = existingCustomer;
		} else {
		    // If customer doesn't exist, create a new one
		    customer = new Customer();
		    customer.setEmail(orderDTO.getCustomer().getEmail());
		    customer.setName(orderDTO.getCustomer().getName());
		    customer.setPassword(generateRandomPassword(8)); // Generating random password
		    customer.setPhno(orderDTO.getCustomer().getPhno());
		    
		    // Save the new customer
		    customer = customerRepo.save(customer);
		}

	    // Step 2: Initialize and Save Order Without OrderItems
	    Order newOrder = new Order();
	    newOrder.setOrderTrackingId(generateOrderTrackingId());
	    newOrder.setTotalQuantity(orderDTO.getTotalQuantity());
	    newOrder.setTotalPrice(orderDTO.getTotalPrice());
	    newOrder.setStatus("packing");
	    newOrder.setDeliveryDate(LocalDate.now().plusDays(4));
	    newOrder.setCustomer(customer);

	    Address address = new Address();
	    address.setStreet(orderDTO.getAddress().getStreet());
	    address.setCity(orderDTO.getAddress().getCity());
	    address.setState(orderDTO.getAddress().getState());
	    address.setZipCode(orderDTO.getAddress().getZipCode());
	    address.setCustomer(customer);
	    
	    
	    newOrder.setAddress(address);//save the address to the order

	    Order savedOrder = orderRepo.save(newOrder); // Save Order without items

	    // Step 3: Create OrderItems and associate them after Order is saved
	    Set<OrderItems> orderItems = new HashSet<>();
	    for (OrderItemsDTO itemDTO : orderDTO.getOrderItems()) {
	        OrderItems newItem = new OrderItems();
	        newItem.setImageUrl(itemDTO.getImageUrl());
	        newItem.setUnitPrice(itemDTO.getUnitPrice());
	        newItem.setQuantity(itemDTO.getQuantity());
	        newItem.setProductId(itemDTO.getProductId());

	        // Now associate each item with the saved order
	        newItem.setOrder(savedOrder);  // Set reference to already saved order
	        orderItems.add(newItem);
	    }

	    orderItemsRepo.saveAll(orderItems); // Save all OrderItems with references to Order

	    // Optionally, associate items with order if needed for further use
//	    savedOrder.setOrderItems(orderItems);
	    orderRepo.save(savedOrder); // Update order to include order items if required

	    return "Order Details are saved with order id value " + savedOrder.getOrderTrackingId();
	}



	@Override
	public OrderDTO getOrderById(Long id) {
	    // Retrieve the Order by ID
	    Optional<Order> optOrder = orderRepo.findById(id);
	    if (optOrder.isPresent()) {
	        Order order = optOrder.get();

	        // Initialize OrderDTO and copy properties from Order
	        OrderDTO orderDTO = new OrderDTO();
	        BeanUtils.copyProperties(order, orderDTO);

	        // Retrieve and map Customer to CustomerDTO
	        Customer customer = order.getCustomer();
	        if (customer != null) {
	            CustomerDTO customerDTO = new CustomerDTO();
	            BeanUtils.copyProperties(customer, customerDTO);
	            orderDTO.setCustomer(customerDTO);
	        }

	        // Retrieve and map Address to AddressDTO
	        Address address = order.getAddress();
	        if (address != null) {
	            AddressDTO addressDTO = new AddressDTO();
	            BeanUtils.copyProperties(address, addressDTO);
	            orderDTO.setAddress(addressDTO);
	        }

	        // Retrieve and map OrderItems to Set of OrderItemsDTO
	        List<OrderItems> items = orderItemsRepo.findByOrderId(id);
	        Set<OrderItemsDTO> itemsDTOSet = new HashSet<>();
	        for (OrderItems item : items) {
	            OrderItemsDTO itemDTO = new OrderItemsDTO();
	            BeanUtils.copyProperties(item, itemDTO);
	            itemsDTOSet.add(itemDTO);
	        }
	        orderDTO.setOrderItems(itemsDTOSet);

	        return orderDTO;
	    }

	    // Handle case when the order is not found
	    throw new IllegalArgumentException("Order not found with order ID " + id);
	}
	
	
	@Override
	public List<OrderDTO> getOrderByUser(String email) {
	    // Retrieve the Customer by email
	    Customer customer = customerRepo.findByEmail(email);
	    if (customer == null) {
	        throw new IllegalArgumentException("Customer not found with email " + email);
	    }

	    // Retrieve the Orders associated with the Customer
	    List<Order> orders = orderRepo.findByCustomerId(customer.getId());
	    if (orders.isEmpty()) {
	        throw new IllegalArgumentException("No orders found for customer with email " + email);
	    }

	    // Create a list to hold OrderDTOs
	    List<OrderDTO> orderDTOList = new ArrayList<>();

	    for (Order order : orders) {
	        // Initialize OrderDTO and copy properties from Order
	        OrderDTO orderDTO = new OrderDTO();
	        BeanUtils.copyProperties(order, orderDTO);

	        // Retrieve and map Address to AddressDTO
	        Address address = order.getAddress();
	        if (address != null) {
	            AddressDTO addressDTO = new AddressDTO();
	            BeanUtils.copyProperties(address, addressDTO);
	            orderDTO.setAddress(addressDTO);
	        }

	        // Retrieve and map OrderItems to Set of OrderItemsDTO
	        List<OrderItems> items = orderItemsRepo.findByOrderId(order.getId());
	        Set<OrderItemsDTO> itemsDTOSet = new HashSet<>();
	        for (OrderItems item : items) {
	            OrderItemsDTO itemDTO = new OrderItemsDTO();
	            BeanUtils.copyProperties(item, itemDTO);
	            itemsDTOSet.add(itemDTO);
	        }
	        orderDTO.setOrderItems(itemsDTOSet);

	        // Add the OrderDTO to the list
	        orderDTOList.add(orderDTO);
	    }

	    return orderDTOList;
	}


	
	 private static String generateOrderTrackingId() {
	        return UUID.randomUUID().toString().replace("-", "").substring(0, 15);
	    }

	 private static String generateRandomPassword(int length) {
	        final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	        SecureRandom random = new SecureRandom();
	        StringBuilder password = new StringBuilder(length);

	        for (int i = 0; i < length; i++) {
	            int index = random.nextInt(CHARACTERS.length());
	            password.append(CHARACTERS.charAt(index));
	        }

	        return password.toString();
	    }






}
