package com.nt.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nt.dto.FormInputDTO;
import com.nt.dto.OrderDTO;
import com.nt.service.IAdminService;

@RestController
@RequestMapping("/admin")
@CrossOrigin
public class AdminRestController {
	
	@Autowired
	private IAdminService ser;
	
	@PostMapping("/find")
    public List<OrderDTO> findOrders(@RequestBody FormInputDTO formInputDTO) {
        return ser.findAllOrders(formInputDTO);
    }

}
