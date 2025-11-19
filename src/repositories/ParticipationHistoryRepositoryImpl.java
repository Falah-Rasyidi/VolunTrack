package repositories;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Participation;
import model.Project;

public class ParticipationHistoryRepositoryImpl implements ParticipationHistoryRepository {

    private final String DB_PATH = "jdbc:sqlite:src/database/voluntrack.db";

    @Override
    public ObservableList<Participation> getParticipations(String username) {
        try (Connection connection = DriverManager.getConnection(DB_PATH); Statement statement = connection.createStatement()) {
            String query = String.format("SELECT participation_id, project.project_id, project.project_title, project.project_location, project.project_day, project.hourly_value, project.registered_slots, project.total_slots, project.enabled, username, participation_date, num_slots, total_hours, total_contribution FROM participation JOIN project ON participation.project_id=project.project_id WHERE username='%s'", username);
            ResultSet resultSet = statement.executeQuery(query);

            ObservableList<Participation> participations = FXCollections.observableArrayList();
            while (resultSet.next()) {
                Project project = new Project(resultSet.getInt(2), // project ID
                                              resultSet.getString(3), // project title
                                              resultSet.getString(4), // project location
                                              resultSet.getString(5), // project day
                                              resultSet.getDouble(6), // project rate
                                              resultSet.getInt(7), // project reg slots
                                              resultSet.getInt(8), // project total slots
                                              resultSet.getInt(9) == 1
                );

                Participation participation = new Participation(resultSet.getInt(1), // participation ID
                                                                project,
                                                                resultSet.getString(10), // username
                                                                resultSet.getString(11), // participation date
                                                                resultSet.getInt(12), // num slots
                                                                resultSet.getInt(13), // total hours
                                                                resultSet.getInt(14) // total contribution
                );

                participations.add(participation);
            }

            return participations;
        } catch (SQLException e) {
            System.out.println("Error in ViewHistoryRepositoryImpl.getParticipations: " + e.getClass().getName());
            e.printStackTrace();
        }

        return FXCollections.observableArrayList();
    }

    @Override
    public ObservableList<Participation> getAllParticipations() {
        try (Connection connection = DriverManager.getConnection(DB_PATH); Statement statement = connection.createStatement()) {
            String query = "SELECT participation_id, project.project_id, project.project_title, project.project_location, project.project_day, project.hourly_value, project.registered_slots, project.total_slots, project.enabled, username, participation_date, num_slots, total_hours, total_contribution FROM participation JOIN project ON participation.project_id=project.project_id;";
            ResultSet resultSet = statement.executeQuery(query);

            ObservableList<Participation> participations = FXCollections.observableArrayList();
            while (resultSet.next()) {
                Project project = new Project(resultSet.getInt(2), // project ID
                                              resultSet.getString(3), // project title
                                              resultSet.getString(4), // project location
                                              resultSet.getString(5), // project day
                                              resultSet.getDouble(6), // project rate
                                              resultSet.getInt(7), // project reg slots
                                              resultSet.getInt(8), // project total slots
                                              resultSet.getInt(9) == 1
                );

                Participation participation = new Participation(resultSet.getInt(1), // participation ID
                                                                project,
                                                                resultSet.getString(10), // username
                                                                resultSet.getString(11), // participation date
                                                                resultSet.getInt(12), // num slots
                                                                resultSet.getInt(13), // total hours
                                                                resultSet.getInt(14) // total contribution
                );

                participations.add(participation);
            }

            return participations;
        } catch (SQLException e) {
            System.out.println("Error in ViewHistoryRepositoryImpl.getParticipations: " + e.getClass().getName());
            e.printStackTrace();
        }

        return FXCollections.observableArrayList();
    }
}
