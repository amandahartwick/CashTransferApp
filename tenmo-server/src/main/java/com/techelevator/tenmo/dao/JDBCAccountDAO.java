package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.tenmo.model.Account;

@Component
public class JDBCAccountDAO implements AccountDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCAccountDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<Account> getAccountByAccountId(int accountId) {
		List<Account> allAccounts = new ArrayList<>();
		String sqlGetAccountById = "SELECT *, FROM accounts, WHERE account_id = ?";

		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAccountById);
		while (results.next()) {
			Account accountResult = mapRowToAccount(results);
			allAccounts.add(accountResult);
		}
		return allAccounts;
	}



	@Override
	public BigDecimal viewCurrentBalance(int accountId) {
		//Select balance from accounts where account_id = ?
		return null;
	}

	@Override
	public int viewPendingRequests(int accountId) {
		// Selecting status from transfer status where transfer key account_id --> transfers table, 
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
