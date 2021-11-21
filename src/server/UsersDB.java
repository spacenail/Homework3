package server;

import java.sql.*;
import java.util.Optional;


public class UsersDB {
private static Connection connection;
private static Statement statement;

    private UsersDB() {
    }

    public static void connect(){
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:resources/users.db");
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void disconnect(){
        if(statement!=null){
            try {
                statement.close();
            }catch (SQLException e){
                e.getStackTrace();
            }
        }

        if(connection!=null){
            try {
                connection.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    public static Optional<String> getUsernameByLoginAndPassword(String login, String password) throws SQLException {
            String sqlQuery = "SELECT * FROM users WHERE login='" + login + "' AND password='" + password + "';";
            ResultSet resultSet = statement.executeQuery(sqlQuery);
            if(resultSet.next()) {
                return Optional.of(resultSet.getString("username"));
            }else {
                return Optional.empty();
            }
    }
}
