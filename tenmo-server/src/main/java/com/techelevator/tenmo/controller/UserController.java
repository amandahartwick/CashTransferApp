package com.techelevator.tenmo.controller;

import java.util.List;

import javax.security.auth.login.AccountNotFoundException;
import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.JDBCTransferDAO;
import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.dao.UserDAO;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.security.jwt.TokenProvider;

@RestController
@PreAuthorize("isAuthenticated()")

public class UserController {

	private final TokenProvider tokenProvider;
	private TransferDAO tDAO;
	private AccountDAO aDAO;
	private UserDAO uDAO;

	public UserController(TokenProvider tokenProvider, TransferDAO tdao, AccountDAO aDAO) {
		this.tokenProvider = tokenProvider;
		this.tDAO = tDAO;
		this.aDAO = aDAO;
		this.uDAO = uDAO;
	}
	
	/**
	 * Retrieves account based on account_id. If user_id in empty or wrong user stays in loop.
	 *
	 * @param account_id -- the user id to be searched for
	 */

	@PreAuthorize ("permitAll())")
	@RequestMapping(value = "account/{account_id}", method = RequestMethod.GET)
	public List<Account> findAccountByAccountId(@Valid @RequestParam(defaultValue = "0") int user_id) throws AccountNotFoundException {
		if(user_id > 0) {
			return aDAO.getAccountByAccountId(user_id);
		}
		return aDAO.findAll();
	}
	
	/**
	 * Retrieves transfer history based on user_id. If user_id in empty or wrong user stays in loop.
	 *
	 * @param user_id -- the user id to be searched for
	 * @param transfer_id --  the transfer id to be searched for
	 */

	@PreAuthorize ("hasAnyRole('ADMIN', 'CREATOR')")
	@RequestMapping(value = "account/{user_id}", method = RequestMethod.GET)
	public List<Account> list(@Valid @RequestParam(defaultValue = "0") int user_id) {
		if(user_id > 0) {
			return aDAO.getAccountByAccountId(user_id);
		}
		System.out.println("Please enter a valid user id.");
	}
}


	
	/*
	 * /8080/account/{user_id} ... get ... user
	 * /8080/transfer/{transfer_id} ... get ... user
	 * 
	 */

}
