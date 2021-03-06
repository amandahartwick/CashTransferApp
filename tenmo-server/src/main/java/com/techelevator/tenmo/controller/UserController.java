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
import com.techelevator.tenmo.model.Transfer;
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
	public boolean findAccountWithUserId(@RequestBody Account account, @RequestParam String username, @RequestParam String password) {
		return uDAO.create(username, password);
	}
	
	/*
	 * Look up user account.
	 * 
	 * @Param user_id
	 */
	@RequestMapping(path = "/{user_id}", method = RequestMethod.GET)
	public Account findAccountWithUserId(@PathVariable int user_id) {
		return aDAO.getAccountByAccountId(user_id);
	}

	/*
	 * Look up all accounts.
	 */
	@RequestMapping(path = "/user", method = RequestMethod.GET)
	public List<Account> findAllAccounts() {
		return aDAO.findAll();
	}
	
	// findAccountbyid
	
	 // *****************
	 // *    TRANSFER   *
	 // *****************
	
	/*
	 * Look up transfer history.
	 * 
	 * @Param account_id
	 */
	@RequestMapping(path = "{user_id}/transfer", method = RequestMethod.GET)
	public List<Transfer> findTransferHistory(@PathVariable int user_id) {
		return tDAO.viewTransferHistory(user_id);
		
		//user id and account id are different so this isnot working
	}

	 // *****************
	 // *    ACCOUNT    *
	 // *****************
	 

	
	}
