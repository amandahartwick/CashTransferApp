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
	 // *   /USERS      *
	 // *****************

	/*
	 * Look up all accounts.
	 */
	@RequestMapping(path = "/users", method = RequestMethod.GET)
	public List<User> findAllUsers() {
		return uDAO.findAll();
	}

	 // *****************
	 // *   /ACCOUNT    *
	 // *****************

	/*
	 * Look up all accounts.
	 */
	@RequestMapping(path = "/accounts", method = RequestMethod.GET)
	public List<Account> findAllAccounts() {
		return aDAO.findAll();
	}

	/*
	 * Look up account details.
	 * 
	 * @Param account_id
	 */
	@RequestMapping(path = "/accounts/{account_id}", method = RequestMethod.GET)
	public Account findAccountWithAccountId(@PathVariable int account_id) {
		return aDAO.getAccountByAccountId(account_id);
	}
	
	/*
	 * Look up account details.
	 * 
	 * @Param account_id
	 */
	@RequestMapping(path = "/accounts/{user_name}", method = RequestMethod.GET)
	public int findAccountWithUserName(@PathVariable String user_name) {
		return uDAO.findIdByUsername(user_name);
	}
	
	/*
	 * Get account balance.
	 * 
	 * @Param account_id
	 */
	@RequestMapping(path = "/accounts/{account_id}/balance", method = RequestMethod.GET)
	public double findCurrentBalance(@PathVariable int account_id) {
		return aDAO.getBalance(account_id);
	}
	/*
	 * Look up user transfer history.
	 * 
	 * @Param account_id
	 */
	@RequestMapping(path = "/accounts/{account_id}/transfers", method = RequestMethod.GET)
	public List<Transfer> accountTransferHistory(@PathVariable int account_id) {
		return tDAO.viewTransferHistory(account_id);
	}
	
	 // *****************
	 // *   /TRANSFER   *
	 // *****************
	
	/*
	 * Send money.
	 * 
	 * @Param accountId_from -- account sending money
	 * @Param request -- amount to send
	 * @Param accountId_to -- account to send money to
	 */

	@RequestMapping(path = "/transfers", method = RequestMethod.GET)
	public boolean sendMoney (@RequestBody Transfer transfer) {
		boolean tdao = false;
		tdao = tDAO.sendBucks(transfer.getAccount_from(), transfer.getAmount(), transfer.getAccount_to());
		return tdao;
	}
	
	/*
	 * Look up transfer details.
	 * 
	 * @Param transfer_id
	 */
	@RequestMapping(path = "/transfers/{transfer_id}", method = RequestMethod.GET)
	public Transfer tansferDetails (@PathVariable int transfer_id) {
		return tDAO.transferDetails(transfer_id);
	}


	
	}
