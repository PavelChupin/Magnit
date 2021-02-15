package com.pavel_chupin.db_support;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {
    private static Connection connection;

    public static Connection getConnection(String url, String user, String pass) throws SQLException, ClassNotFoundException {
        if (connection == null) {
            initConnect(url, user, pass);
        }
        return connection;
    }

    private static void initConnect(String url, String user, String pass) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection(url, user, pass);
        //connection = DriverManager.getConnection(getProperty(DATABASE_URL));
        if (!connection.isClosed()) {
            System.out.println("Connect to DB");
        }
        //Отключаем у соединения автокоммит
        connection.setAutoCommit(false);

        System.out.println("Database ConnToDataBase Established...");
    }

    public static void closeConnection() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}
