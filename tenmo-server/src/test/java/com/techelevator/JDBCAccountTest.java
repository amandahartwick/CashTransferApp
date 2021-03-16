package com.techelevator;
import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.List;
import org.junit.*;
import org.junit.Assert;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.tenmo.dao.JDBCAccountDAO;
import com.techelevator.tenmo.model.Account;


public class JDBCAccountTest {
	private static final int TA1 = 1;
	private static final int TA2 = 2;
	private static final int TU1 = 1;
	private static final int TU2 = 2;
	private static SingleConnectionDataSource dataSource;
	private static final double BALANCE = 1000.00;
	private JDBCAccountDAO dao;

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
		//String truncate = "TRUNCATE TABLE accounts CASCADE";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		//jdbcTemplate.update(truncate);
		dao = new JDBCAccountDAO(jdbcTemplate);
		
		String SqlAdd = "INSERT INTO accounts (balance, account_id, user_id)"
				+ " VALUES (?, ?, ?);";
		jdbcTemplate.update(SqlAdd, BALANCE, TA1, TU1);
		jdbcTemplate.update(SqlAdd, BALANCE, TA2, TU2);
		

	}

	@Test
	public void find_all_test() {
		List<Account> all = dao.findAll();
		Assert.assertEquals(all.get(0).getAccountId(), TA1);
		Assert.assertEquals(all.get(all.size() - 1).getAccountId(),TA2);
	
	}

	@Test
	public void get_account_by_id() {
		Account mine = dao.getAccountByAccountId(TA1);
		Assert.assertEquals(mine.getUserId(), TU1);

	}

	@Test
	public void get_account_by_user_test() {
		Account mine = dao.getAccountByUserId(TU1);
		Assert.assertEquals(mine.getAccountId(), TA1);
	}

	@Test
	public void get_balance_test() {
		double balance = dao.getBalance(TA1);
		Assert.assertEquals(1000.00, balance, 0);
		

	}

	private Account getAccount(int user_id, int account_id, double balance) {
		Account acct = new Account();
		acct.setUserId(user_id);
		acct.setAccountId(account_id);
		acct.setBalance(balance);
		return acct;
	}

	private void assertAccountsAreEqual(Account expected, Account actual) {
		assertEquals(expected.getAccountId(), actual.getAccountId());
		assertEquals(expected.getUserId(), actual.getUserId());
		assertEquals(expected.getBalance(), actual.getBalance(), 0);
		
		
		
		
	}
}
