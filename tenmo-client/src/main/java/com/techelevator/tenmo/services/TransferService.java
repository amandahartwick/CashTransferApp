package com.techelevator.tenmo.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.models.Transfer;

public class TransferService {

	public static final String API_TRANSFER_URL = "http://localhost:8080/api/v1";
	public RestTemplate restTemplate = new RestTemplate();
	public static String AUTH_TOKEN = "";

	public List<Transfer> viewMyTransferHistory(int accountId) {
		List<Transfer> transfers = new ArrayList<Transfer>();
		try {
			Transfer[] transArray = restTemplate.exchange(API_TRANSFER_URL + "/accounts/" + accountId + "/transfers",
					HttpMethod.GET, makeAuthEntity(), Transfer[].class).getBody();
			for (Transfer t : transArray) {
				transfers.add(t);
			}
		} catch (RestClientResponseException ex) {
			System.out.println("Bad Input");
			ex.printStackTrace();
		}
		return transfers;
	}

	public Transfer transferDetails(int transferId) {
		Transfer details = null;
		try {
			details = restTemplate.exchange(API_TRANSFER_URL + "/transfers/" + transferId, HttpMethod.GET,
					makeAuthEntity(), Transfer.class).getBody();
		} catch (RestClientResponseException ex) {
			System.out.println("Bad Input");
			ex.printStackTrace();
			;
		}
		return details;
	}

	public Transfer sendBucks(int user_id, double amount, int receiver_id) {
		Transfer success = new Transfer();
		success.setAccount_from(user_id);
		success.setAccount_to(receiver_id);
		success.setAmount(amount);
		try {
			success = restTemplate
					.exchange(API_TRANSFER_URL + "/transfers", HttpMethod.POST, makeTransferEntity(success), Transfer.class)
					.getBody();
		} catch (RestClientResponseException ex) {
			System.out.println("Bad Input");
			ex.printStackTrace();
		}
		return success;
	}

	private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(AUTH_TOKEN);
		HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
		return entity;
	}

	private HttpEntity makeAuthEntity() {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(AUTH_TOKEN);
		HttpEntity entity = new HttpEntity<>(headers);
		return entity;
	}

}
