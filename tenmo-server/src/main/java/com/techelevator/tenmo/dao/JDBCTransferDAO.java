package com.techelevator.tenmo.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.InsufficientFundException;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.dao.JDBCAccountDAO;

@Component
public class JDBCTransferDAO implements TransferDAO {

	private JdbcTemplate jdbcTemplate;
	private AccountDAO aDAO;

	public JDBCTransferDAO(JdbcTemplate jdbcTemplate, AccountDAO jdbcAccountDAO) {
		this.jdbcTemplate = jdbcTemplate;
		this.aDAO = jdbcAccountDAO;
	}

	/*
	 * sends money from_user to to_user and manages balances
	 *
	 * perhaps it would be a more simplistic approach to have a "moneyChange" method
	 * which takes care of balance changes? ^ execute if calculations are needed
	 * elsewhere ^
	 */
	@Override
	public void sendBucks(int accountId_from, double request, int accountId_to) {
		//Logic
		Account sender = aDAO.getAccountByAccountId(accountId_from);
		Account reciever = aDAO.getAccountByAccountId(accountId_to);
		if(sender.getBalance() >= request) {
			double senderBalance = sender.getBalance() - request;
			sender.setBalance(senderBalance);
			double receiverBalance = reciever.getBalance() + request;
			reciever.setBalance(receiverBalance);
			//Transfer built
			long transferId = getNextTransferId();
			String sqlTransfer = "INSERT INTO transfers (transfer_id, transfer_status_id, transfer_type_id, account_from, account_to, amount)"
					+ "VALUES (?, 2, 2, ?, ?, ?);";
			jdbcTemplate.update(sqlTransfer, transferId, sender.getAccountId(), reciever.getAccountId(), request);
			//Update accounts
			String sqlUpdateSender = "UPDATE accounts SET balance = ? WHERE account_id = ?;";
			jdbcTemplate.update(sqlUpdateSender, sender.getBalance(), sender.getAccountId());
			String sqlUpdateReciever = "UPDATE accounts SET balance = ? WHERE account_id = ?;";
			jdbcTemplate.update(sqlUpdateReciever, reciever.getBalance(), reciever.getAccountId());
			
		} else {
			System.out.println("Insufficient Funds");
			//throw new InsufficientFundException();
		}
		
	}

	// view entire transfer history of a user by account_id
	//CHECK SQL STATEMENT
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
	
	//CHECK SQL STATEMENT
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

	// view all pending requests based on *account_from* -- OPTIONAL USECASE
	@Override
	public List<Transfer> viewPendingRequests(int accountId) {
		List<Transfer> pendingRequests = new ArrayList<>();
		String sqlRetreivePendingRequests = "SELECT transfer_id, transfer_type_id, transfer_status_id account_from, account_to, amount FROM transfers WHERE transfer_status_id = \"Pending\" AND account_from = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlRetreivePendingRequests, accountId);
		while (results.next()) {
			Transfer transferResult = mapRowToTransfer(results);
			pendingRequests.add(transferResult);
		}
		return pendingRequests;
	}
	
	@Override
	public void requestBucks(int accountId_from, double request, int accountId_to) {
		// TODO Auto-generated method stub
		
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
