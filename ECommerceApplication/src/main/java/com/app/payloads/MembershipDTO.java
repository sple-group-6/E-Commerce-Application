package com.app.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MembershipDTO {
	private Long membershipId;
	private String membershipCode;
	private double discountPercentage;
}
