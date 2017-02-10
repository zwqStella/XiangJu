package com.atom.socketServer.DAOImple;

import java.sql.*;

import com.atom.socketServer.DAO.UserDAO;
import com.atom.socketServer.presisitence.Account;
import com.atom.socketServer.util.DBUtil;

public class UserDAOImple implements UserDAO{
	private static final String GET_USER_BY_USERNAME = "SELECT * FROM user WHERE username = ?";
	private static final String INSERT_NEW_USER = "INSERT INTO user (username, password, email, isactivated) VALUES (?, ?, ?, ?)";
	private static final String UPDATE_USER_ACTIVATE_BY_USERNAME = "UPDATE user SET isactivated = 1 WHERE username = ?";
	
	@Override
	public String loginByUsernameAndPassWord(Account account) {
		// TODO Auto-generated method stub
		Account secletedAccount = null;
		try{
            Connection connection = DBUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_BY_USERNAME);
            preparedStatement.setString(1,account.getAccountName());
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next())
            {
            	secletedAccount = new Account(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4));
            }

            DBUtil.closeResultSet(resultSet);
            DBUtil.closePreparedStatement(preparedStatement);
            DBUtil.closeConnection(connection);

        }catch(Exception e)
        {
        	e.printStackTrace();
        }
		if(secletedAccount == null){
			return "x00;false";
		}
		if(!secletedAccount.getPassword().equals(secletedAccount.getPassword())){
			return "x01;false";
		}else if(!secletedAccount.getIsativated().equals("1")){
			return "x02;false";
		}
		else{
			//System.out.println("√‹¬Î¥ÌŒÛ£°");
			return "c11;true";
		}
	}


	@Override
	public String registerByUsernameAndPasswordAndEmail(Account account) {
		// TODO Auto-generated method stub

		try{
            Connection connection = DBUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_NEW_USER);
            preparedStatement.setString(1,account.getAccountName());
            preparedStatement.setString(2,account.getPassword());
            preparedStatement.setString(3,account.getEmail());
            preparedStatement.setString(4,"0");
            preparedStatement.executeUpdate();

            DBUtil.closePreparedStatement(preparedStatement);
            DBUtil.closeConnection(connection);
            return "true";
        }catch(Exception e)
        {
            e.printStackTrace();
            return "false";        
        }

	}


	@Override
	public String activateUserByUsername(Account account) {
		// TODO Auto-generated method stub
		try{
            Connection connection = DBUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER_ACTIVATE_BY_USERNAME);
            preparedStatement.setString(1,account.getAccountName());
            preparedStatement.executeUpdate();

            DBUtil.closePreparedStatement(preparedStatement);
            DBUtil.closeConnection(connection);
            return "true";
        }catch(Exception e)
        {
            e.printStackTrace();
            return "false";        
        }

	
	}

}
