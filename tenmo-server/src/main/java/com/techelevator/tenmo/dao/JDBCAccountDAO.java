package com.techelevator.tenmo.dao;

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

	// return all accounts
	@Override
	public List<Account> findAll() {
			List<Account> allAccounts = new ArrayList<>();
			String sqlGetAccountById = "SELECT balance, account_id, user_id FROM accounts";
			SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAccountById);
			while (results.next()) {
				Account accountResult = mapRowToAccount(results);
				allAccounts.add(accountResult);
			}
			return allAccounts;
	}
	// find balance based on account_id
	@Override
	public double getBalance(int accountId) {
		double currentBalance = 0.0;
		String sqlGetcurrentBalance = "SELECT balance, account_id, user_id FROM accounts WHERE account_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetcurrentBalance, accountId);
		while (results.next()) {
			Account accountResult = mapRowToAccount(results);
			currentBalance = accountResult.getBalance();
		}
		return currentBalance;
	}
	// find account by account_id
	@Override
	public Account getAccountByAccountId(int accountId) {
		Account theAccount = new Account(); 
		String sqlGetAccountById = "SELECT balance, account_id, user_id FROM accounts WHERE account_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAccountById, accountId);
		while(results.next()) {
			theAccount = mapRowToAccount(results);
		}
		return theAccount;
	}

	// find account by user_id
	@Override
	public Account getAccountByUserId(int userId) {
		Account allAccounts = null;
		String sqlGetAccountById = "SELECT balance, account_id, user_id FROM accounts WHERE user_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAccountById, userId);
		while (results.next()) {
			allAccounts = mapRowToAccount(results);
		}
		return allAccounts;
	}



	private Account mapRowToAccount(SqlRowSet result) {
		Account acct = new Account();
		acct.setUserId(result.getInt("user_id"));
		acct.setAccountId(result.getInt("account_id"));
		acct.setBalance(result.getDouble("balance"));
		return acct;
	}

}
