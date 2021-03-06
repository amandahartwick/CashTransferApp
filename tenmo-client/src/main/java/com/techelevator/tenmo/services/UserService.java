package com.techelevator.tenmo.services;

import java.util.ArrayList;
import java.util.List;

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
					HttpMethod.POST, makeAuthEntity(), boolean.class).getBody();
		} catch (RestClientResponseException e) {
			throw new UserServiceException(e.getRawStatusCode() + " : " + e.getResponseBodyAsString());
		}
		return didItCreate;
	};

	List<User> findAll() throws UserServiceException {
		List<User> users = new ArrayList<>();
		try {
			User[] list = restTemplate.exchange(BASE_URL + "", HttpMethod.GET, makeAuthEntity(), User[].class)
					.getBody();
			for (User u : list) {
				users.add(u);
			}
		} catch (RestClientResponseException ex) {
			throw new UserServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		}
		return users;

	} // THIS METHOD NEEDS AN UPDATED URL BASED ON THE API

	User findById() throws UserServiceException {
		User idUser = null;
		try {
			idUser = restTemplate.exchange(BASE_URL + "/UPDATEME", HttpMethod.GET, makeAuthEntity(), User.class)
					.getBody();
		} catch (RestClientResponseException ex) {
			throw new UserServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		}

		return idUser;
	}
	// THIS METHOD NEEDS AN UPDATED URL BASED ON THE API

	User findIdByUserName() throws UserServiceException {
		User theUser = null;
		try {
			theUser = restTemplate.exchange(BASE_URL + "/UPDATEME", HttpMethod.GET, makeAuthEntity(), User.class)
					.getBody();
		} catch (RestClientResponseException ex) {
			throw new UserServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		}

		return theUser;
	}
	// THIS METHOD NEEDS AN UPDATED URL BASED ON THE API
	
	String findUserNameById() throws UserServiceException{
		User theUser = null;
		try {
			theUser = restTemplate.exchange(BASE_URL + "/UPDATEME", HttpMethod.GET, makeAuthEntity(), User.class)
					.getBody();
		} catch (RestClientResponseException ex) {
			throw new UserServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		}

		return theUser.getUsername();
	}
	// THIS METHOD NEEDS AN UPDATED URL BASED ON THE API
	
	
	

	private HttpEntity<User> makeUserEntity(User user) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(AUTH_TOKEN);
		HttpEntity<User> entity = new HttpEntity<>(user, headers);
		return entity;
	}

	private HttpEntity makeAuthEntity() {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(AUTH_TOKEN);
		HttpEntity entity = new HttpEntity<>(headers);
		return entity;
	}

}
