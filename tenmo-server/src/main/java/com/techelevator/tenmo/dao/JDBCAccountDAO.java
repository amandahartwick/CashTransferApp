package com.techelevator.tenmo.dao;

import java.util.ArrayList;
import java.util.List;

import javax.activation.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.tenmo.BasicDataSource;
import com.techelevator.tenmo.model.Account;

import org.springframework.jdbc.core.JdbcTemplate;

public class JDBCAccountDAO implements AccountDAO{
		
	private JdbcTemplate jdbcTemplate;
	
	public JDBCAccountDAO(DataSource datasource) {
		this.jdbcTemplate = new JdbcTemplate(datasource);
	}
	
	@Override
	public List<Account> getAccountByAccountId(int accountId) {
		List<Account> allAccounts = new ArrayList<>();
		String sqlGetAccountById = "SELECT *, FROM accounts, WWHERE account_id = ?";
		
		SQLRowSet results = jdbcTemplate.queryForRowSet(sqlGetAccountById);
		while (results.next()) {
			Account accountResult = mapRowToAccount(results);
			allAccounts.add(accountResult);
		}
		return allAccounts;
	}
	
	public List<Account> retreiveTransferHistory(int accountId) {

		
		return null;
	}
	
	private Account mapRowToAccount(SqlRowSet result) {
		Account acct = new Account();
		acct.setUserId(result.getInt("user_id"));
		acct.setAccountId(result.getInt("account_id"));
		acct.setBalance(result.getBigDecimal("balance"));
		return acct;
	}
}
