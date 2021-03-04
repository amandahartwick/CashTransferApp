package com.techelevator.tenmo.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class JDBCTransferDAO implements TransferDAO {
	
	private JdbcTemplate jdbcTemplate;

	public JDBCTransferDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void sendBucks() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestBucks() {
		// TODO Auto-generated method stub
		
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

}
