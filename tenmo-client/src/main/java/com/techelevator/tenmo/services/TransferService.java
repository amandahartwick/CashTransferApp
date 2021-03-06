package com.techelevator.tenmo.services;

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

	public static final String API_TRANSFER_URL = "http://localhost:8080/transfer";
	public RestTemplate restTemplate = new RestTemplate();
	public static String AUTH_TOKEN = "";


	// THIS METHOD NEEDS AN UPDATED URL BASED ON THE API
	public List<Transfer> viewMyTransferHistory(int accountId) throws TransferServiceException {
		List<Transfer> transfers = null;
		try {
			Transfer[] transArray = restTemplate.exchange(API_TRANSFER_URL + "/" + accountId + "/history",
					HttpMethod.GET, makeAuthEntity(), Transfer[].class).getBody();
			for (Transfer t : transArray) {
				transfers.add(t);
			}
		} catch (RestClientResponseException ex) {
			throw new TransferServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		}
		return transfers;
	}
	
	
	
	
	
	// THIS METHOD NEEDS AN UPDATED URL BASED ON THE API
	public Transfer sendBucks(int user_id, int receiver_id) {
		Transfer newTransfer = null;
		return newTransfer;
	}

	
	
	
	
	// THIS METHOD NEEDS AN UPDATED URL BASED ON THE API
	public Transfer transferDetails(int transferId) throws TransferServiceException {
		Transfer details = null;
		try {
			details = restTemplate
					.exchange(API_TRANSFER_URL + "/" + transferId, HttpMethod.GET, makeAuthEntity(), Transfer.class)
					.getBody();
		} catch (RestClientResponseException ex) {
			throw new TransferServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		}
		return details;
	}
	
	
	
	
	
	
//	public Transfer[] viewPendingRequests(int accountId) {
//	Transfer[] transfers = null;
//	// THIS IS AN OPTIONAL METHOD
//	return transfers;
//}

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
