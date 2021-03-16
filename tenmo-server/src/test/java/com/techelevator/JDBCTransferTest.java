package com.techelevator;

import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.JDBCAccountDAO;
import com.techelevator.tenmo.dao.JDBCTransferDAO;
import com.techelevator.tenmo.model.Transfer;


public class JDBCTransferTest {
	private static final int TA1 = 1;
	private static final int TA2 = 2;
	private static final double BALANCE = 1000.00;
	private static final int TU1 = 1;
	private static final int TU2 = 2;
	private static SingleConnectionDataSource dataSource;
	private JDBCTransferDAO dao;
	private AccountDAO aDAO;
	
	
	
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
		aDAO = new JDBCAccountDAO(jdbcTemplate);
		dao = new JDBCTransferDAO(jdbcTemplate, aDAO);
		//String truncatetransfer = "TRUNCATE TABLE transfers CASCADE";
		//jdbcTemplate.update(truncatetransfer);
		String SqlAdd = "INSERT INTO accounts (balance, account_id, user_id)"
				+ " VALUES (?, ?, ?);";
		jdbcTemplate.update(SqlAdd, BALANCE, TA1, TU1);
		jdbcTemplate.update(SqlAdd, BALANCE, TA2, TU2);
		String sqlTransfer = "INSERT INTO transfers (transfer_id, transfer_status_id, transfer_type_id, account_from, account_to, amount)"
				+ "VALUES (?, 2, 2, ?, ?, ?);";
		jdbcTemplate.update(sqlTransfer, 1, TA1, TA2, 500.00);
				
	}
	
	@Test
	public void send_bucks_test() {
		dao.sendBucks(TA1, 500.00, TA2);
		
		Assert.assertEquals(aDAO.getBalance(TA1), 500.00, 0);
		Assert.assertEquals(aDAO.getBalance(TA2), 1500.00, 0);
	}
	
	@Test
	public void send_fail() {
		dao.sendBucks(TA2, 1001.00, TA1);
		
		Assert.assertEquals(aDAO.getBalance(TA1), 1000.00, 0);
		Assert.assertEquals(aDAO.getBalance(TA2), 1000.00, 0);
	}
	
	@Test
	public void get_transfer_history() {
		List<Transfer> transferHistory = dao.viewTransferHistory(TA1);
		
		Assert.assertEquals(transferHistory.get(0).getAccount_from(), TA1);
		Assert.assertEquals(transferHistory.get(0).getAccount_to(), TA2);
		Assert.assertEquals(transferHistory.get(0).getAmount(), 500.00, 0);
	}
	
	@Test
	public void view_transfer_details() {
		Transfer transferDetails = dao.transferDetails(1);
		Assert.assertEquals(transferDetails.getAmount(), 500.00, 0);
		Assert.assertEquals(transferDetails.getTransfer_status_id(), 2);
		Assert.assertEquals(transferDetails.getTransfer_type_id(), 2);
		Assert.assertEquals(transferDetails.getAccount_from(), TA1);
		Assert.assertEquals(transferDetails.getAccount_to(), TA2);
		
	}
	
	
	
}
