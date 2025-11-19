package repositories;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class PasswordChangeRepositoryImpl implements PasswordChangeRepository {

    private final String DB_PATH = "jdbc:sqlite:src/database/voluntrack.db";

    @Override
    public boolean changePassword(String username, String newPassword) {
        try (Connection connection = DriverManager.getConnection(DB_PATH); Statement statement = connection.createStatement()) {
            String query = String.format("UPDATE user SET password_hash = '%s' WHERE username = '%s'", newPassword, username);
            statement.executeUpdate(query);

            return true;
        } catch (SQLException e) {
            System.out.println("Error in PasswordChangeRepositoryImpl.changePassword: " + e.getClass().getName());
            e.printStackTrace();
        }

        return false;
    }
}
