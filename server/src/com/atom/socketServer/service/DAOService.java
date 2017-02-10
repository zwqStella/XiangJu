package com.atom.socketServer.service;

import com.atom.socketServer.DAO.UserDAO;
import com.atom.socketServer.DAOImple.UserDAOImple;
import com.atom.socketServer.presisitence.Account;

public class DAOService {
	private static UserDAO userDAO = new UserDAOImple();
	
	public static String loginByUsernameAndPassWord(Account account){
		return userDAO.loginByUsernameAndPassWord(account);
	}
	public static String registerByUsernameAndPasswordAndEmail(Account account){
		return userDAO.registerByUsernameAndPasswordAndEmail(account);
	}
	public static String activateUser(Account account){
		return userDAO.activateUserByUsername(account);
	}
}
