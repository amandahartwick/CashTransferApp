package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDAO {
	public void sendBucks(int account_id, BigDecimal request, int accountId2);

	public void requestBucks(int account_id, BigDecimal request, int accountId2);
	
	public List<Transfer> viewTransferHistory(int accountId);
	
	public List<Transfer> viewPendingRequests(int accountId);

		
}
