package com.atom.socketServer.util;

import java.sql.*;


public class DBUtil {
    private static String driverString = "com.mysql.jdbc.Driver";
    private static String connectionString = "jdbc:mysql://localhost:3306/share_location_system";
    private static String userName = "root";
    private static String passWord = "212eyldgu37915";

    public static Connection getConnection() throws Exception {
        Connection connection = null;
        try
        {
            Class.forName(driverString);
            connection = DriverManager.getConnection(connectionString,userName,passWord);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return connection;
    }
    public static void closeStatement(Statement statement) throws Exception
    {
        statement.close();
    }
    public static void closePreparedStatement(PreparedStatement preparedStatement) throws Exception
    {
        preparedStatement.close();
    }
    public static void closeResultSet(ResultSet resultSet) throws Exception
    {
        resultSet.close();
    }
    public static void closeConnection(Connection connection)throws Exception
    {
        connection.close();
    }

    /* test DB connection
    public static void main(String[] args) throws Exception
    {
        Connection connection = DBUtil.getConnection();
        System.out.println(connection);
    }*/
}