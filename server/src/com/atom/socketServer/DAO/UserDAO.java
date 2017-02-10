package com.atom.socketServer.DAO;

import com.atom.socketServer.presisitence.Account;

public interface UserDAO {
	public String loginByUsernameAndPassWord(Account account);
	public String registerByUsernameAndPasswordAndEmail(Account account);
	public String activateUserByUsername(Account account);

}
