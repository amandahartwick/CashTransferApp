package com.techelevator.tenmo.dao;

import java.math.BigDecimal;

public interface TransferDAO {
	public void sendBucks(int account_id, BigDecimal request, int accountId2);

	public void requestBucks(int account_id, BigDecimal request, int accountId2);

		
}
