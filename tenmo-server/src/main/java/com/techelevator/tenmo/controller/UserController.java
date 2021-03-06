package com.techelevator.tenmo.controller;

import java.util.List;

import javax.security.auth.login.AccountNotFoundException;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.dao.UserDAO;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.security.jwt.TokenProvider;

@RestController

public class UserController {

	private final TokenProvider tokenProvider;
	private TransferDAO tDAO;
	private AccountDAO aDAO;
	private UserDAO uDAO;

	public UserController(TokenProvider tokenProvider, AccountDAO aDAO, UserDAO uDAO, TransferDAO tDAO) {
		this.tokenProvider = tokenProvider;
		this.tDAO = tDAO;
		this.aDAO = aDAO;
		this.uDAO = uDAO;
	}
	
	 // *****************
	 // *     USER      *
	 // *****************
	
	/*
	 * Look up user account.
	 * 
	 * @Param username
	 * @Param password
	 */
	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(path = "/user", method = RequestMethod.POST)
	public boolean findAccountWithUserId(@RequestBody Account account) {
		return uDAO.create();
	}
	
	/*
	 * Look up user account.
	 * 
	 * @Param user_id
	 */
	@RequestMapping(path = "/user/{user_id}", method = RequestMethod.GET)
	public Account findAccountWithUserId(@PathVariable int user_id) {
		return aDAO.getAccountByAccountId(user_id);
	}


	
	// findAccountbyid
	
	 // *****************
	 // *    TRANSFER   *
	 // *****************
	
	
	
	 // *****************
	 // *    ACCOUNT    *
	 // *****************
	 
	/*
	// Retrieves all accounts
    @PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(path = "/account", method = RequestMethod.GET)
	public List<Account> findAllAccounts () {
		if(user_id > 0) {
			return aDAO.getAccountByUserId(user_id);
		}
		return aDAO.findAll();
	}
	*/
	
	}
