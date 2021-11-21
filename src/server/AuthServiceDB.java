package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class AuthServiceDB {
private static Connection connection;
private static Statement statement;

    private AuthServiceDB() {
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



}
