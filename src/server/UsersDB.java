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
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT username FROM users WHERE login=? AND password=?;");
            preparedStatement.setString(1,login);
            preparedStatement.setString(2,password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return Optional.of(resultSet.getString("username"));
            }else {
                return Optional.empty();
            }
    }

    static boolean changeUsername(String currentUserName,String newUserName) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE users SET username = ? WHERE username = ?");
        preparedStatement.setString(2,currentUserName);
        preparedStatement.setString(1,newUserName);
        return preparedStatement.execute();
    }
}
