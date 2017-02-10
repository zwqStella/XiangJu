package com.atom.socketServer.presisitence;

public class Account {
	private String accountName;
	private String password;
	private String email;
	private String isativated;
	
	
	public Account() {
	}
	public Account(String accountName, String password, String email,String isativated) {
		super();
		this.accountName = accountName;
		this.password = password;
		this.email = email;
		this.isativated = isativated;
	}
	public Account(String accountName, String password, String email) {
		super();
		this.accountName = accountName;
		this.password = password;
		this.email = email;
	}
	public Account(String accountName, String password) {
		super();
		this.accountName = accountName;
		this.password = password;
	}
	public Account(String accountName) {
		super();
		this.accountName = accountName;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getIsativated() {
		return isativated;
	}
	public void setIsativated(String isativated) {
		this.isativated = isativated;
	}
	
	
}
