package db;

import model.Item;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DbManager {
    private static Connection connection;
    static {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/postgres?currentSchema=sprint2?currentSchema=sprint2",
                    "postgres",
                    "postgres"
            );
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static List<Item>getItems(){
        List<Item>items = new ArrayList<>();
        try {
                var statement = connection.prepareStatement(
                    "SELECT  * FROM ITEMS"
            );
            var resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Item item = new Item();
                item.setId(resultSet.getLong("ID"));
                item.setName(resultSet.getString("NAME"));
                item.setDescription(resultSet.getString("DESCRIPTION"));
                item.setPrice(resultSet.getDouble("PRICE"));
                items.add(item);
            }
            statement.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }

    public static String auth(String email, String password) {
        User user = getUSerByEmail(email);
        if (user == null) {
            return "emailError";
        }
        if (!Objects.equals(user.getPassword(),password)){
            return "passwordError";

        }
        return "success";
    }

    public static User getUSerByEmail(String email){
        User user = null;
        try {
            var statement = connection.prepareStatement(
                    "SELECT  * FROM USERS " +
                            "WHERE EMAIL = ?"
            );
            statement.setString(1, email);
            var resultSet = statement.executeQuery();
            if (resultSet.next()){
                user = new User();
                user.setId(resultSet.getLong("ID"));
                user.setEmail(email);
                user.setPassword(resultSet.getString("PASSWORD"));
                user.setFullName(resultSet.getString("FULL_NAME"));
            }
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }
}
