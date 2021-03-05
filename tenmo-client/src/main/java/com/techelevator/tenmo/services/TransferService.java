package com.techelevator.tenmo.services;

import java.util.List;

import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.models.Transfer;

public class TransferService {

	public static final String API_TRANSFER_URL = "http://localhost:8080/transfer";
	public RestTemplate restTemplate = new RestTemplate();
	
	public Transfer[] viewPendingRequests(int accountId){
		Transfer[] transfers = null;
		//THIS IS AN OPTIONAL METHOD
		return transfers;
	}
	
	public Transfer[] viewTransferHistory(int accountId) {
		Transfer[] transfers = null;
		try {
			transfers = restTemplate.getForObject(API_TRANSFER_URL + "/" + accountId + "/history", Transfer[].class);
		} catch (RestClientResponseException ex) {
			System.out.println("Could not retrieve the auctions. Is the server running?");
		} catch (ResourceAccessException ex) {
			System.out.println("A network error occurred.");
		}
		return transfers;
	}
	
	public BigDecimal sendBucks(int user_id, BigDecimal gift, int receiver_id) {
		
		//user_id
		//amount to send
	}
	
}
