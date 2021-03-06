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
	private final String BASE_URL = "http://localhost:8080/api/v1";
	private final RestTemplate restTemplate = new RestTemplate();

	public boolean create(String username, String password) {
		boolean didItCreate = false;
		try {
			didItCreate = restTemplate.exchange(BASE_URL + "/user" + "?username=" + username + "&?password=" + password,
					HttpMethod.POST, makeAuthEntity(), boolean.class).getBody();
		} catch (RestClientResponseException e) {
			System.out.println("Bad Input");
		}
		return didItCreate;
	};

	List<User> findAllUsers() {
		List<User> users = new ArrayList<>();
		try {
			User[] list = restTemplate.exchange(BASE_URL + "/users", HttpMethod.GET, makeAuthEntity(), User[].class)
					.getBody();
			for (User u : list) {
				users.add(u);
			}
		} catch (RestClientResponseException ex) {
			System.out.println("Bad Input");
		}
		return users;

	} // DWNT

	User findById(int user_id) {
		User idUser = null;
		try {
			idUser = restTemplate.exchange(BASE_URL + "/UPDATEME", HttpMethod.GET, makeAuthEntity(), User.class)
					.getBody();
		} catch (RestClientResponseException ex) {
			System.out.println("Bad Input");
		}

		return idUser;
	}
	// DWNT

	User findIdByUserName() {
		User theUser = null;
		try {
			theUser = restTemplate.exchange(BASE_URL + "/UPDATEME", HttpMethod.GET, makeAuthEntity(), User.class)
					.getBody();
		} catch (RestClientResponseException ex) {
			System.out.println("Bad Input");
		}

		return theUser;
	}
	// DWNT

	String findUserNameById() {
		User theUser = null;
		try {
			theUser = restTemplate.exchange(BASE_URL + "/UPDATEME", HttpMethod.GET, makeAuthEntity(), User.class)
					.getBody();
		} catch (RestClientResponseException ex) {
			System.out.println("Bad Input");
		}

		return theUser.getUsername();
	}

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
