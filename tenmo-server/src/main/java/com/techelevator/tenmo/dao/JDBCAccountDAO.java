package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.tenmo.model.Account;

public class JDBCAccountDAO implements AccountDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCAccountDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<Account> getAccountByAccountId(int accountId) {
		List<Account> allAccounts = new ArrayList<>();
		String sqlGetAccountById = "SELECT *, FROM accounts, WWHERE account_id = ?";

		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAccountById);
		while (results.next()) {
			Account accountResult = mapRowToAccount(results);
			allAccounts.add(accountResult);
		}
		return allAccounts;
	}

	public List<Account> viewTransferHistory(int accountId) {

		return null;
	}

	@Override
	public BigDecimal viewCurrentBalance(int accountId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int viewPendingRequests(int accountId) {
		// TODO Auto-generated method stub
		return 0;
	}

	private Account mapRowToAccount(SqlRowSet result) {
		Account acct = new Account();
		acct.setUserId(result.getInt("user_id"));
		acct.setAccountId(result.getInt("account_id"));
		acct.setBalance(result.getBigDecimal("balance"));
		return acct;
	}

}
