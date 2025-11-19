package repositories;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import model.Participation;
import model.Project;
import model.Registration;

public class CartRepositoryImpl implements CartRepository {

    private final String DB_PATH = "jdbc:sqlite:src/database/voluntrack.db";

    @Override
    public ObservableMap<Integer, Registration> retrieveCart(String username) {
        try (Connection connection = DriverManager.getConnection(DB_PATH); Statement statement = connection.createStatement()) {
            String query = String.format("SELECT project.project_id, project.project_title, project.project_location, project_day, project.hourly_value, project.registered_slots, project.total_slots, project.enabled, registration.registration_id, registration.username, registration.slot_one_hours, registration.slot_two_hours, registration.slot_three_hours FROM registration JOIN project ON registration.project_id=project.project_id WHERE username='%s'", username);
            ResultSet resultSet = statement.executeQuery(query);

            ObservableMap<Integer, Registration> cart = FXCollections.observableHashMap();
            while (resultSet.next()) {
                Project project = new Project(resultSet.getInt(1),
                                              resultSet.getString(2),
                                              resultSet.getString(3),
                                              resultSet.getString(4),
                                              resultSet.getDouble(5),
                                              resultSet.getInt(6),
                                              resultSet.getInt(7),
                                              resultSet.getInt(8) == 1
                );
                
                Registration registration = new Registration(resultSet.getInt(9),
                                                             project,
                                                             resultSet.getString(10),
                                                             resultSet.getInt(11),
                                                             resultSet.getInt(12),
                                                             resultSet.getInt(13)
                );

                cart.put(project.getProjectID(), registration);
            }

            // Delete registration from table
            query = String.format("DELETE FROM registration WHERE username='%s'", username);
            statement.executeUpdate(query);

            return cart;
        } catch (SQLException e) {
            System.out.println("Error in CartRepositoryImpl.retrieveCart: " + e.getClass().getName());
            e.printStackTrace();
        }

        return FXCollections.observableHashMap();
    }

    @Override
    public void checkoutCart(ArrayList<Participation> cart) {
        try (Connection connection = DriverManager.getConnection(DB_PATH); Statement statement = connection.createStatement()) {
            String query = "INSERT INTO participation (participation_id, project_id, username, participation_date, num_slots, total_hours, total_contribution) VALUES ";

            for (int i = 0; i < cart.size(); i++) {
                if (i == cart.size() - 1) {
                    query += String.format("(%d, %d, '%s', '%s', %d, %d, %f);", cart.get(i).getParticipationID(), cart.get(i).getProject().getProjectID(), cart.get(i).getUsername(), cart.get(i).getParticipationDate(), cart.get(i).getNumSlots(), cart.get(i).getTotalHours(), cart.get(i).getTotalContribution());
                }
                else {
                    query += String.format("(%d, %d, '%s', '%s', %d, %d, %f), ", cart.get(i).getParticipationID(), cart.get(i).getProject().getProjectID(), cart.get(i).getUsername(), cart.get(i).getParticipationDate(), cart.get(i).getNumSlots(), cart.get(i).getTotalHours(), cart.get(i).getTotalContribution());
                }
            }

            statement.executeUpdate(query);

        } catch (SQLException e) {
            System.out.println("Error in CartRepositoryImpl.checkoutCart: " + e.getClass().getName());
            e.printStackTrace();
        }
    }

    @Override
    public void saveCart(ArrayList<Registration> cart) {
        try (Connection connection = DriverManager.getConnection(DB_PATH); Statement statement = connection.createStatement()) {
            String query = "INSERT INTO registration (project_id, username, slot_one_hours, slot_two_hours, slot_three_hours) VALUES ";

            for (int i = 0; i < cart.size(); i++) {
                if (i == cart.size() - 1) {
                    query += String.format("(%d, '%s', %d, %d, %d);", cart.get(i).getProject().getProjectID(), cart.get(i).getUsername(), cart.get(i).getSlotOneHours(), cart.get(i).getSlotTwoHours(), cart.get(i).getSlotThreeHours());
                }
                else {
                    query += String.format("(%d, '%s', %d, %d, %d), ", cart.get(i).getProject().getProjectID(), cart.get(i).getUsername(), cart.get(i).getSlotOneHours(), cart.get(i).getSlotTwoHours(), cart.get(i).getSlotThreeHours());
                }
            }

            statement.executeUpdate(query);

        } catch (SQLException e) {
            System.out.println("Error in CartRepositoryImpl.saveCart: " + e.getClass().getName());
            e.printStackTrace();
        }
    }
}
