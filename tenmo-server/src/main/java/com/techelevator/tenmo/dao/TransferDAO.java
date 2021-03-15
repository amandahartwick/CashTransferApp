package com.techelevator.tenmo.dao;

import java.util.List;

import com.techelevator.tenmo.model.Transfer;

public interface TransferDAO {
	public boolean sendBucks(int accountId_from, double request, int accountId_to);

	public boolean requestBucks(int accountId_from, double request, int accountId_to);
	
	public List<Transfer> viewTransferHistory(int accountId);
	
	public Transfer transferDetails(int transferId);

	public List<Transfer> viewPendingRequests(int accountId);

	public boolean acceptTransfer(int transferId, int userId, int accepted);
}
