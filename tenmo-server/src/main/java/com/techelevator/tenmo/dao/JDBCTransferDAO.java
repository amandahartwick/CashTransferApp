package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.tenmo.model.InsufficientFundException;
import com.techelevator.tenmo.model.Transfer;

@Component
public class JDBCTransferDAO implements TransferDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCTransferDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/*
	 * sends money from_user to to_user and manages balances
	 *
	 * perhaps it would be a more simplistic approach to have a "moneyChange" method
	 * which takes care of balance changes? ^ execute if calculations are needed
	 * elsewhere ^
	 */
	@Override
	public void sendBucks(int account_id, BigDecimal request, int account_id2) {
		BigDecimal theBalance;
		String sqlBalance = "SELECT account_id, user_id, balance FROM accounts WHERE account_id = ?;";
		SqlRowSet balanceResult = jdbcTemplate.queryForRowSet(sqlBalance, account_id);
		if (balanceResult.next()) {
			theBalance = balanceResult.getBigDecimal("balance");
			BigDecimal newBalance = theBalance.subtract(request);
			BigDecimal zero = BigDecimal.valueOf(0.0);
			if (newBalance.compareTo(zero) >= 0) {
				Long transferId = getNextTransferId();
				String sqlTransfer = "INSERT INTO transfers (transfer_id, transfer_status_id, transfer_type_id, account_from, account_to, amount)"
						+ "VALUES (?, 2, 2, ?, ?, ?);";
				jdbcTemplate.update(sqlTransfer, transferId, account_id, account_id2, request);
				String sqlUpdateSender = "UPDATE accounts SET balance = ? WHERE account_id = ?;";
				jdbcTemplate.update(sqlUpdateSender, newBalance, account_id);
				String sqlBalanceReciever = "SELECT account_id, user_id, balance FROM accounts WHERE account_id = ?;";
				SqlRowSet recieverBalanceResult = jdbcTemplate.queryForRowSet(sqlBalanceReciever, account_id2);
				if(recieverBalanceResult.next()) {  //This will probably cause a problem
					BigDecimal currentBalance = recieverBalanceResult.getBigDecimal("balance");
					currentBalance.add(request);
					String sqlTransferToReceiver = "UPDATE accounts SET balance = ? WHERE account_id = ?;";
					jdbcTemplate.update(sqlTransferToReceiver, currentBalance, account_id2);					
				}

				// Create Transfer
				// Update both balances
			} else {
				throw new InsufficientFundException();
			}
		}
	}

	@Override
	public void requestBucks(int account_id, BigDecimal request, int accountId2) {
		//

	}
	
	// view entire transfer history of a user by account_id
	@Override
	public List<Transfer> viewTransferHistory(int accountId) {
		List<Transfer> tansferHistory = new ArrayList<>();
		String sqlGetTransferHistory = "SELECT transfer_history, transfer_type_id, transfer_status_id, account_from, account_to, amount FROM transfers WHERE account_from = ?";
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
		String sqlGetTransferDetails = "SELECT transfer_history, transfer_type_id, transfer_status_id, account_from, account_to, amount FROM transfers WHERE transfer_id = ?";
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
	

	private Transfer mapRowToTransfer(SqlRowSet results) {
		Transfer theTransfer;
		theTransfer = new Transfer();
		theTransfer.setAccount_from(results.getInt("account_from"));
		theTransfer.setAccount_to(results.getInt("account_to"));
		theTransfer.setAmount(results.getBigDecimal("amount"));
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
