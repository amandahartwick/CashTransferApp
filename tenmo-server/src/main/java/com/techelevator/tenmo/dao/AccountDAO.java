package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.List;

import com.techelevator.tenmo.model.Account;

public interface AccountDAO {
	
	public List<Account> getAccountByAccountId(int accountId);
	
	public List<Account> viewTransferHistory(int accountId);

	public BigDecimal viewCurrentBalance(int accountId);
	
	public int viewPendingRequests(int accountId);
	
	
}
