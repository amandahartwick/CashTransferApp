package com.techelevator.tenmo.dao;

import java.util.List;

import com.techelevator.tenmo.model.Account;

public interface AccountDAO {
	
	public List<Account> findAll();	
	
	public Account getAccountByAccountId(int accountId);
	
	public Account getAccountByUserId(int userId);

	public double getBalance(int accountId);	
}