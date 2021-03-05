package com.techelevator;

import java.math.BigDecimal;
import java.sql.SQLException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.tenmo.model.Account;

public class JDBCAccountTest {
	private static final int TA1 = 1;
	private static final int TA2 = 2;
	private static SingleConnectionDataSource dataSource;
	private static final double BALANCE = 1000.00;

	@BeforeClass
	public static void setupDataSource() {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/tenmo");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		dataSource.setAutoCommit(false);
	}

	@AfterClass
	public static void closeDataSource() throws SQLException {
		dataSource.destroy();
	}

	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}

	@Before
	public void setup() {

	}

	@Test
	public void find_all_test() {

	}

	@Test
	public void get_account_by_id() {

	}

	@Test
	public void get_account_by_username_test() {

	}

	@Test
	public void get_balance_test() {

	}

	private Account getAccount(int user_id, int account_id, double balance) {
		Account acct = new Account();
		acct.setUserId(user_id);
		acct.setAccountId(account_id);
		acct.setBalance(balance);
		return acct;
	}

}
