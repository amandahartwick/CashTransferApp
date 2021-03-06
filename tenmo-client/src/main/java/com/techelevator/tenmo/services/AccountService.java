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

	public static final String API_ACCOUNT_URL = "http://localhost:8080/api/v1";
	public RestTemplate restTemplate = new RestTemplate();
	public static String AUTH_TOKEN = "";

	List<Account> findAllAccounts() {
		List<Account> accounts = new ArrayList<>();
		try {
			Account[] list = restTemplate
					.exchange(API_ACCOUNT_URL + "/accounts", HttpMethod.GET, makeAuthEntity(), Account[].class)
					.getBody();
			for (Account u : list) {
				accounts.add(u);
			}
		} catch (RestClientResponseException ex) {
			ex.printStackTrace();
		}
		return accounts;

	}

	public Account findAccountWithAccountId(int accountId) {
		Account account = null;
		try {
			account = restTemplate.exchange(API_ACCOUNT_URL + "/accounts/" + accountId, HttpMethod.GET,
					makeAuthEntity(), Account.class).getBody();
		} catch (RestClientResponseException ex) {
			System.out.println("Could not retrieve the account.");
			ex.printStackTrace();
		} catch (ResourceAccessException ex) {
			System.out.println("A network error occurred.");
			ex.printStackTrace();
		}
		return account;

	}

	// DWNT
	public Account getAccountbyUserID(int userId) {
		Account account = null;
		try {
			account = restTemplate
					.exchange(API_ACCOUNT_URL + "/accounts/" + userId, HttpMethod.GET, makeAuthEntity(), Account.class)
					.getBody();
		} catch (RestClientResponseException ex) {
			System.out.println("Could not retrieve the account.");
			ex.printStackTrace();
		} catch (ResourceAccessException ex) {
			System.out.println("A network error occurred.");
			ex.printStackTrace();
		}
		return account;

	}

	public double viewCurrentBalance(int accountId) {
		Account account = null;
		try {
			account = restTemplate.exchange(API_ACCOUNT_URL + "/accounts/" + accountId + "/balance", HttpMethod.GET,
					makeAuthEntity(), Account.class).getBody();
		} catch (RestClientResponseException ex) {
			System.out.println("Could not retrieve the account.");
			ex.printStackTrace();
		} catch (ResourceAccessException ex) {
			System.out.println("A network error occurred.");
			ex.printStackTrace();
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
