package server;

import java.sql.*;
import java.util.Optional;


class DB {
private static Connection connection;
private static Statement statement;

    private DB() {
    }

    static void connect(){
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:resources/users.db");
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void disconnect(){
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

    static Optional<String> getUsernameByLoginAndPassword(String login, String password){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT username FROM users WHERE login=? AND password=?;");
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(resultSet.getString("username"));
            }
        }catch (SQLException e){
            e.printStackTrace();
            }
        return Optional.empty();
    }

    static boolean isUsernameOccupied(String username) {
        boolean status = false;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM logged_users WHERE username=?;");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            status = resultSet.next();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return status;
    }

    static boolean changeUsername(String currentUserName,String newUserName){
        boolean status = false;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE users SET username = ? WHERE username = ?");
            preparedStatement.setString(2, currentUserName);
            preparedStatement.setString(1, newUserName);
            status = preparedStatement.execute();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return status;
    }

    static boolean addUserToLoggedUsers(String user) {
        boolean status = false;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT logged_users SET username = ?");
            preparedStatement.setString(1, user);
            status = preparedStatement.execute();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return status;
    }

    static boolean deleteUserFromLoggedUsers(String user){
        boolean status = false;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM logged_users WHERE username = ?");
            preparedStatement.setString(1, user);
            status = preparedStatement.execute();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return status;
    }
}
