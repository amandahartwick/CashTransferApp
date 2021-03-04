package com.techelevator.tenmo;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.JDBCAccountDAO;
import com.techelevator.tenmo.dao.JDBCTransferDAO;
import com.techelevator.tenmo.dao.TransferDAO;



@SpringBootApplication
public class TenmoApplication {
	private AccountDAO accountDAO;
	private TransferDAO transferDAO;

	TenmoApplication(){
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/tenmo");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		
		accountDAO = new JDBCAccountDAO(dataSource);
		transferDAO = new JDBCTransferDAO(dataSource);
	}
    public static void main(String[] args) {
        SpringApplication.run(TenmoApplication.class, args);
        
    }

}
