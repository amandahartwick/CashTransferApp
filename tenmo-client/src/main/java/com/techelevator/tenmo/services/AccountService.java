package com.techelevator.tenmo.services;
import java.math.BigDecimal;

import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.models.Account;

public class AccountService {

	public static final String API_ACCOUNT_URL = "http://localhost:8080/account";
	public RestTemplate restTemplate = new RestTemplate();
	
	
	public Account getAccountbyID(int accountId) {
		Account account = null;
		try {
			account = restTemplate.getForObject(API_ACCOUNT_URL + "/" + accountId, Account.class);
		} catch (RestClientResponseException ex) {
			System.out.println("Could not retrieve the account.");
		} catch (ResourceAccessException ex) {
			System.out.println("A network error occurred.");
		}
		return account;
		
	}
	
	public BigDecimal viewCurrentBalance(int accountId) {
		Account account = null;
		try {
			account = restTemplate.getForObject(API_ACCOUNT_URL + "/" + accountId + "/balance", Account.class);
		} catch (RestClientResponseException ex) {
			System.out.println("Could not retrieve the account.");
		} catch (ResourceAccessException ex) {
			System.out.println("A network error occurred.");
		}
		return account.getBalance();
		
	}
	
	
	
	

}
