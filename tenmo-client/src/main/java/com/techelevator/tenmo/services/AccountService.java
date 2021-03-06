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

import com.techelevator.tenmo.models.Account;

public class AccountService {

	public static final String API_ACCOUNT_URL = "http://localhost:8080/account";
	public RestTemplate restTemplate = new RestTemplate();
	public static String AUTH_TOKEN = "";
	
	
	// THIS METHOD NEEDS AN UPDATED URL BASED ON THE API
	List<Account> findAll() throws AccountServiceException {
		List<Account> accounts = new ArrayList<>();
		try {
			Account[] list = restTemplate
					.exchange(API_ACCOUNT_URL + "", HttpMethod.GET, makeAuthEntity(), Account[].class).getBody();
			for (Account u : list) {
				accounts.add(u);
			}
		} catch (RestClientResponseException ex) {
			throw new AccountServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		}
		return accounts;

	}

	
	// THIS METHOD NEEDS AN UPDATED URL BASED ON THE API
	public Account getAccountbyAccountID(int accountId) {
		Account account = null;
		try {
			account = restTemplate
					.exchange(API_ACCOUNT_URL + "/" + accountId, HttpMethod.GET, makeAuthEntity(), Account.class)
					.getBody();
		} catch (RestClientResponseException ex) {
			System.out.println("Could not retrieve the account.");
		} catch (ResourceAccessException ex) {
			System.out.println("A network error occurred.");
		}
		return account;

	}

	
	
	// THIS METHOD NEEDS AN UPDATED URL BASED ON THE API
	public Account getAccountbyUserID(int userId) {
		Account account = null;
		try {
			account = restTemplate
					.exchange(API_ACCOUNT_URL + "/" + userId, HttpMethod.GET, makeAuthEntity(), Account.class)
					.getBody();
		} catch (RestClientResponseException ex) {
			System.out.println("Could not retrieve the account.");
		} catch (ResourceAccessException ex) {
			System.out.println("A network error occurred.");
		}
		return account;

	}

	
	
	// THIS METHOD NEEDS AN UPDATED URL BASED ON THE API
	public double viewCurrentBalance(int accountId) {
		Account account = null;
		try {
			account = restTemplate.exchange(API_ACCOUNT_URL + "/" + accountId + "/balance", HttpMethod.GET,
					makeAuthEntity(), Account.class).getBody();
		} catch (RestClientResponseException ex) {
			System.out.println("Could not retrieve the account.");
		} catch (ResourceAccessException ex) {
			System.out.println("A network error occurred.");
		}
		return account.getBalance();

	}

	private HttpEntity<Account> makeUserEntity(Account account) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(AUTH_TOKEN);
		HttpEntity<Account> entity = new HttpEntity<>(account, headers);
		return entity;
	}

	private HttpEntity makeAuthEntity() {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(AUTH_TOKEN);
		HttpEntity entity = new HttpEntity<>(headers);
		return entity;
	}

}
