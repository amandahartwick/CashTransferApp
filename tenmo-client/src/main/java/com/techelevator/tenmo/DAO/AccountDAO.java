package com.techelevator.tenmo.DAO;

import java.awt.List;

public interface AccountDAO {
	
	public void create();
	
	public void delete();
	
	public List<Account> searchAccountsByUserId(int UserId);
	
	

}
