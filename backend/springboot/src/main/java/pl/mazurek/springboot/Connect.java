package pl.mazurek.springboot;

import java.sql.*;

public class Connect {

    public static Connection connectToDb() {

        Connection connection = null;

        try {
            String url = "jdbc:sqlite:C:/Projects/springboot/transactions.db";
            connection = DriverManager.getConnection(url);
            System.out.println("Connected to db");

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return connection;
    }
}
