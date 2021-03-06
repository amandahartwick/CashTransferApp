package com.techelevator.tenmo.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.models.User;



public class UserService {
	public static String AUTH_TOKEN = "";
	private final String BASE_URL = "http://localhost:8080/";
	private final RestTemplate restTemplate = new RestTemplate();
//	private final ConsoleService console = new ConsoleService();

	public boolean create(String username, String password) throws UserServiceException {
		boolean didItCreate = false;
		try {
			didItCreate = restTemplate.exchange(BASE_URL + "user" + "?username=" + username + "&?password=" + password,
					HttpMethod.POST, makeUserEntity(), boolean.class).getBody();
		} catch (RestClientResponseException e) {
			throw new UserServiceException(e.getRawStatusCode() + " : " + e.getResponseBodyAsString());
		}
		return didItCreate;
	};

	private HttpEntity<User> makeUserEntity(User user) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(AUTH_TOKEN);
		HttpEntity<User> entity = new HttpEntity<>(user, headers);
		return entity;
	}

	private HttpEntity makeUserEntity() {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(AUTH_TOKEN);
		HttpEntity entity = new HttpEntity<>(headers);
		return entity;
	}

}
