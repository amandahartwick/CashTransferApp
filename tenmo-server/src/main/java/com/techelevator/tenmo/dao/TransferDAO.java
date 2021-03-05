package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.List;

import com.techelevator.tenmo.model.Transfer;

public interface TransferDAO {
	public void sendBucks(int account_id, BigDecimal request, int accountId2);

	public void requestBucks(int account_id, BigDecimal request, int accountId2);
	
	public List<Transfer> viewTransferHistory(int accountId);
	
	public Transfer transferDetails(int transferId);
	
	// OPTIONAL
	public List<Transfer> viewPendingRequests(int accountId);
}
