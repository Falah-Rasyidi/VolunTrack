package repositories;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import model.User;

public class UserRepositoryImpl implements UserRepository {

    private final String DB_PATH = "jdbc:sqlite:src/database/voluntrack.db";

    @Override
    public List<User> getUsers() {
        try (Connection connection = DriverManager.getConnection(DB_PATH); Statement statement = connection.createStatement()) {
            String query = String.format("SELECT * FROM user");
            ResultSet resultSet = statement.executeQuery(query);

            List<User> userList = new ArrayList<User>();
            while (resultSet.next()) {
                userList.add(new User(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
                ));
            }

            return userList;
        } catch (SQLException e) {
            System.out.println("Error in UserRepositoryImpl.getUsers: " + e.getClass().getName());
            e.printStackTrace();
        }

        return new ArrayList<User>();
    }

    @Override
    public boolean addUser(User newUser) {
        try (Connection connection = DriverManager.getConnection(DB_PATH); Statement statement = connection.createStatement()) {
            String query = String.format("INSERT INTO user (fullname, username, email, password_hash, user_type) VALUES ('%s', '%s', '%s', '%s', '%s');", newUser.getFullname(), newUser.getUsername(), newUser.getEmail(), newUser.getPasswordHash(), newUser.getUserType());
            statement.executeUpdate(query);

            return true;
        } catch (SQLException e) {
            System.out.println("Error in UserRepositoryImpl.addUser: " + e.getClass().getName());
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public Optional<User> findUser(String username) {
        try (Connection connection = DriverManager.getConnection(DB_PATH); Statement statement = connection.createStatement()) {
            String query = String.format("SELECT * FROM user WHERE username = '%s';", username);
            ResultSet resultSet = statement.executeQuery(query);
            
            if (!resultSet.isBeforeFirst()) {
                return Optional.empty();
            }

            User user = new User(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5));
            return Optional.of(user);
        } catch (SQLException e) {
            System.out.println("Error in UserRepositoryImpl.findUser: " + e.getClass().getName());
            e.printStackTrace();
        }

        return Optional.empty();
    }
}
