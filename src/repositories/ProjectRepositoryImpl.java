package repositories;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javafx.collections.ObservableMap;
import model.Project;

public class ProjectRepositoryImpl implements ProjectRepository {

    private final String DB_PATH = "jdbc:sqlite:src/database/voluntrack.db";

    @Override
    public void updateAllProjects(ObservableMap<String, ArrayList<Project>> projects) {
        try (Connection connection = DriverManager.getConnection(DB_PATH); Statement statement = connection.createStatement()) {
            for (ArrayList<Project> projectList : projects.values()) {
                for (int i = 0; i < projectList.size(); i++) {
                    String query = String.format("UPDATE project SET enabled=%d WHERE project.project_id=%d;", projectList.get(i).getEnabled() ? 1 : 0, projectList.get(i).getProjectID());
                    statement.executeUpdate(query);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error in ProjectRepositoryImpl.updateAllProjects: " + e.getClass().getName());
            e.printStackTrace();
        }
    }

    @Override
    public void addProject(String projectTitle, String projectLocation, String projectDay, double hourlyRate, int totalSlots) {
        try (Connection connection = DriverManager.getConnection(DB_PATH); Statement statement = connection.createStatement()) {
            String query = String.format("INSERT INTO project (project_title, project_location, project_day, hourly_value, registered_slots, total_slots, enabled) VALUES ('%s', '%s', '%s', %f, %d, %d, %d);", projectTitle, projectLocation, projectDay, hourlyRate, 0, totalSlots, 1);
            statement.executeUpdate(query);

        } catch (SQLException e) {
            System.out.println("Error in ProjectRepositoryImpl.addProject: " + e.getClass().getName());
            e.printStackTrace();
        }
    }

    @Override
    public void modifyProject(Project project) {
        try (Connection connection = DriverManager.getConnection(DB_PATH); Statement statement = connection.createStatement()) {
            String query = String.format("UPDATE project SET project_title='%s', project_location='%s', project_day='%s', hourly_value=%f, total_slots=%d WHERE project_id=%d;", project.getProjectTitle(), project.getProjectLocation(), project.getProjectDay(), project.getHourlyValue(), project.getTotalSlots(), project.getProjectID());
            statement.executeUpdate(query);

        } catch (SQLException e) {
            System.out.println("Error in ProjectRepositoryImpl.modifyProject: " + e.getClass().getName());
            e.printStackTrace();
        }
    }

    @Override
    public boolean findDuplicateProject(int projectID, String projectTitle, String projectLocation, String projectDay) {
        try (Connection connection = DriverManager.getConnection(DB_PATH); Statement statement = connection.createStatement()) {
            String query = String.format("SELECT * FROM project WHERE project_id != %d AND lower(project_title) LIKE lower('%s') AND lower(project_location) LIKE lower('%s') AND project_day='%s';", projectID, projectTitle, projectLocation, projectDay);
            ResultSet resultSet = statement.executeQuery(query);
            
            return resultSet.isBeforeFirst();
        } catch (SQLException e) {
            System.out.println("Error in ProjectRepositoryImpl.findDuplicateProject: " + e.getClass().getName());
            e.printStackTrace();
        }

        return false;
    }
}
