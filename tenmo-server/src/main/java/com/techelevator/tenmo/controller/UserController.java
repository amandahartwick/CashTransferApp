package com.techelevator.tenmo.controller;

import java.util.List;

import javax.security.auth.login.AccountNotFoundException;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
import com.techelevator.tenmo.model.RegisterUserDTO;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserAlreadyExistsException;
import com.techelevator.tenmo.model.UsesrAlreadyExistsException;
import com.techelevator.tenmo.security.jwt.TokenProvider;

@RestController
@RequestMapping("/api/v1")

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
	 // *   /REGISTER   *
	 // *****************
	
	/*
	 * Register new user account.
	 */
	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(path = "/register", method = RequestMethod.POST)
	public void findAccountWithUserId(@Valid @RequestBody RegisterUserDTO newUser) {
		try { uDAO.findByUsername(newUser.getUsername());
			throw new UserAlreadyExistsException();
		} catch (UsernameNotFoundException notFoundExeption) {
			uDAO.create(newUser.getUsername(), newUser.getPassword());
		}
	}
	
	 // *****************
	 // *     /USER     *
	 // *****************

	/*
	 * Look up all accounts.
	 */
	@RequestMapping(path = "/users", method = RequestMethod.GET)
	public List<Account> findAllAccounts() {
		return aDAO.findAll();
	}

	/*
	 * Look up user account.
	 * 
	 * @Param user_id
	 */
	@RequestMapping(path = "/users/{user_id}", method = RequestMethod.GET)
	public Account findAccountWithUserId(@PathVariable int user_id) {
		return aDAO.getAccountByAccountId(user_id);
	}


	
	// findAccountbyid
	
	 // *****************
	 // *   /TRANSFER   *
	 // *****************
	
	/*
	 * Look up transfer history.
	 * 
	 * @Param account_id
	 */
	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(path = "/transfer/sendmoney", method = RequestMethod.POST)
	public void sendMoney(@Valid @RequestBody Transfer transfer, int accountId_from, double request, int accountId_to) {
		tDAO.sendBucks(accountId_from, accountId_from, accountId_to);
	}

	 // *****************
	 // *   /ACCOUNT    *
	 // *****************
	 

	
	}
