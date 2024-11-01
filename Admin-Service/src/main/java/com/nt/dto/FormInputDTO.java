package com.nt.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class FormInputDTO {
	private String email;
	private LocalDate startDate;
	private LocalDate endDate;
	private String orderTrackingId;
}
