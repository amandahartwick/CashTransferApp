package com.techelevator.tenmo.dao;

import java.util.List;

import com.techelevator.tenmo.model.Transfer;

public interface TransferDAO {
	public void sendBucks(int accountId_from, double request, int accountId_to);

	//OPTIONAL
	public void requestBucks(int accountId_from, double request, int accountId_to);
	
	public List<Transfer> viewTransferHistory(int accountId);
	
	public Transfer transferDetails(int transferId);
	
	// OPTIONAL
	public List<Transfer> viewPendingRequests(int accountId);
}
