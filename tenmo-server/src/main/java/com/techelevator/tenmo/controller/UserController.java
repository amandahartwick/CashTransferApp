package com.techelevator.tenmo.controller;


import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

	import com.techelevator.tenmo.dao.AccountDAO;
	import com.techelevator.tenmo.dao.JDBCTransferDAO;
	import com.techelevator.tenmo.dao.TransferDAO;
	import com.techelevator.tenmo.dao.UserDAO;
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
		
	@RequestMapping(value = "/account/{user_id}/AllTransfers/", method = RequestMethod.GET)
		public List<Transfer> myTransfers(@RequestParam(defaultValue = "0") int user_id) {
			if(user_id > 0) {
				return aDAO.getAccountByUserId(user_id);
			}
			return aDAO.findAll();
	}
	
	/*
	* /8080/account/{user_id}/transfer/{transfer_id} ... get ... user
	* 
	*/
	
}
