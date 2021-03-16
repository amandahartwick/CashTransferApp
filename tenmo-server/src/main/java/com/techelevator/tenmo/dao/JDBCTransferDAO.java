package com.techelevator.tenmo.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

@Component
public class JDBCTransferDAO implements TransferDAO {

	private JdbcTemplate jdbcTemplate;
	private AccountDAO aDAO;

	public JDBCTransferDAO(JdbcTemplate jdbcTemplate, AccountDAO jdbcAccountDAO) {
		this.jdbcTemplate = jdbcTemplate;
		this.aDAO = jdbcAccountDAO;
	}

	@Override
	public boolean sendBucks(int fromUserID, double request, int toUserID) {
		// Logic
		boolean success = false;
		Account sender = aDAO.getAccountByUserId(fromUserID);
		Account reciever = aDAO.getAccountByUserId(toUserID);
		if (sender.getBalance() >= request && sender != reciever && request > 0) {
			double senderBalance = sender.getBalance() - request;
			sender.setBalance(senderBalance);
			double receiverBalance = reciever.getBalance() + request;
			reciever.setBalance(receiverBalance);
			// Transfer built
			long transferId = getNextTransferId();
			String sqlTransfer = "INSERT INTO transfers (transfer_id, transfer_status_id, transfer_type_id, account_from, account_to, amount)"
					+ "VALUES (?, 2, 2, ?, ?, ?);";
			jdbcTemplate.update(sqlTransfer, transferId, sender.getAccountId(), reciever.getAccountId(), request);
			// Update accounts
			String sqlUpdateSender = "UPDATE accounts SET balance = ? WHERE account_id = ?;";
			jdbcTemplate.update(sqlUpdateSender, sender.getBalance(), sender.getAccountId());
			String sqlUpdateReciever = "UPDATE accounts SET balance = ? WHERE account_id = ?;";
			jdbcTemplate.update(sqlUpdateReciever, reciever.getBalance(), reciever.getAccountId());
			success = true;
			System.out.println("Transaction Successful");

		} else {
			System.out.println("Insufficient Funds");
		}
		return success;
	}

	// view entire transfer history of a user by account_id

	@Override
	public List<Transfer> viewTransferHistory(int accountId) {
		List<Transfer> tansferHistory = new ArrayList<>();

		String sqlGetTransferHistory = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount FROM transfers WHERE account_from = ?";

		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetTransferHistory, accountId);
		while (results.next()) {
			Transfer transferResult = mapRowToTransfer(results);
			tansferHistory.add(transferResult);
		}
		return tansferHistory;
	}

	// view all details of a single transfer_id

	@Override
	public Transfer transferDetails(int transferId) {
		Transfer transferDetails = null;
		String sqlGetTransferDetails = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount FROM transfers WHERE transfer_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetTransferDetails, transferId);
		while (results.next()) {
			transferDetails = mapRowToTransfer(results);
		}
		return transferDetails;
	}

	@Override
	public boolean requestBucks(int me, double request, int you) {
		boolean success = false;
		if (request > 0) {

			Account mine = aDAO.getAccountByUserId(me);
			Account yours = aDAO.getAccountByUserId(you);
			// Transfer built
			long transferId = getNextTransferId();
			String sqlTransfer = "INSERT INTO transfers (transfer_id, transfer_status_id, transfer_type_id, account_from, account_to, amount)"
					+ "VALUES (?, 1, 1, ?, ?, ?);";
			jdbcTemplate.update(sqlTransfer, transferId, yours.getAccountId(), mine.getAccountId(), request);
			success = true;
		}
		return success;
	}

	@Override
	public List<Transfer> viewPendingRequests(int accountId) {
		List<Transfer> pendingRequests = new ArrayList<>();
		String sqlRetreivePendingRequests = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount FROM transfers WHERE transfer_status_id = 1 AND account_to = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlRetreivePendingRequests, accountId);
		while (results.next()) {
			Transfer transferResult = mapRowToTransfer(results);
			pendingRequests.add(transferResult);
		}
		return pendingRequests;
	}

	@Override
	public boolean acceptTransfer(int transferId, int userId, int transferStatus) {
		boolean resolved = false;

		Transfer transferRequest = new Transfer();
		String sqlRetrieveTransfer = "SELECT * FROM transfers WHERE transfer_id = ? AND account_from = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlRetrieveTransfer, transferId, userId);
		while (results.next()) {
			transferRequest = mapRowToTransfer(results);
		}
		if (transferRequest != null) {
			Account sender = aDAO.getAccountByAccountId(userId);
			Account reciever = aDAO.getAccountByAccountId(transferRequest.getAccount_to());
			if (sender.getBalance() >= transferRequest.getAmount() && transferRequest.getAmount() > 0 && transferStatus == 2) // transferStatus == 1 was experimental
				{
				sender.setBalance(sender.getBalance() - transferRequest.getAmount());
				reciever.setBalance(reciever.getBalance() + transferRequest.getAmount());
				
				String sqlTransferUpdate = "UPDATE transfers SET transfer_status_id = 2 WHERE transfer_id = ?";
				jdbcTemplate.update(sqlTransferUpdate, transferRequest.getTransfer_id());
				
				String sqlUpdateSender = "UPDATE accounts SET balance = ? WHERE account_id = ?;";
				jdbcTemplate.update(sqlUpdateSender, sender.getBalance(), sender.getAccountId());
				
				String sqlUpdateReciever = "UPDATE accounts SET balance = ? WHERE account_id = ?;";
				jdbcTemplate.update(sqlUpdateReciever, reciever.getBalance(), reciever.getAccountId());
			
				} else if (transferStatus == 3) { // transferStatus == 2 was experimental
				String sqlTransferRejected = "UPDATE transfers SET transfer_status_id = 3 WHERE transfer_id = ?";
				jdbcTemplate.update(sqlTransferRejected, transferRequest.getTransfer_id());

			}
		}
		return resolved;
	}

	private Transfer mapRowToTransfer(SqlRowSet results) {
		Transfer theTransfer;
		theTransfer = new Transfer();
		theTransfer.setAccount_from(results.getInt("account_from"));
		theTransfer.setAccount_to(results.getInt("account_to"));
		theTransfer.setAmount(results.getDouble("amount"));
		theTransfer.setTransfer_id(results.getInt("transfer_id"));
		theTransfer.setTransfer_status_id(results.getInt("transfer_status_id"));
		theTransfer.setTransfer_type_id(results.getInt("transfer_type_id"));

		return theTransfer;
	}

	// set the next transfer_id -- sendBucks() helper method
	private Long getNextTransferId() {
		SqlRowSet nextId = jdbcTemplate.queryForRowSet("SELECT nextval('seq_transfer_id')");
		if (nextId.next()) {
			return nextId.getLong(1);
		} else {
			throw new RuntimeException("Cannot get new ID #");
		}
	}

}
