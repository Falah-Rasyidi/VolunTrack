package database;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import model.Project;

public class ProcessCSV {
    private final String CSV_FILE = "src/database/projects.csv";
    private final String DB_PATH = "jdbc:sqlite:src/database/voluntrack.db";
    
    public ArrayList<Project> loadPropertyData() {
        ArrayList<Project> projects = new ArrayList<Project>();

        // Uncomment if the project table is empty
        // populateDatabase();

        try (Connection connection = DriverManager.getConnection(DB_PATH); Statement statement = connection.createStatement()) {
            String query = String.format("SELECT * from project");
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                projects.add(new Project(
                    resultSet.getInt(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getDouble(5),
                    resultSet.getInt(6),
                    resultSet.getInt(7),
                    resultSet.getInt(8) == 1
                ));
            }

            return projects;
        } catch (SQLException e) {
            System.out.println("Error in ProcessCSV.loadPropertyData: " + e.getClass().getName());
            e.printStackTrace();
        }

        return projects;
    }

    // Map the project title to a list of projects with that same title
    public ObservableMap<String, ArrayList<Project>> loadAdminPropertyData() {
        ObservableMap<String, ArrayList<Project>> projects = FXCollections.observableHashMap();

        // Uncomment if the project table is empty
        // populateDatabase();

        try (Connection connection = DriverManager.getConnection(DB_PATH); Statement statement = connection.createStatement()) {
            String query = String.format("SELECT * from project");
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Project project = new Project(
                    resultSet.getInt(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getDouble(5),
                    resultSet.getInt(6),
                    resultSet.getInt(7),
                    resultSet.getInt(8) == 1
                );

                // Add project to existing project group, otherwise, create new key
                ArrayList<Project> projectGroup = new ArrayList<Project>();
                if (!projects.containsKey(project.getProjectTitle().toLowerCase())) {
                    projectGroup.add(project);
                    projects.put(project.getProjectTitle().toLowerCase(), projectGroup);
                }
                else {
                    projectGroup = projects.get(project.getProjectTitle().toLowerCase());
                    projectGroup.add(project);
                    projects.put(project.getProjectTitle().toLowerCase(), projectGroup);
                }
            }

            return projects;
        } catch (SQLException e) {
            System.out.println("Error in ProcessCSV.loadPropertyData: " + e.getClass().getName());
            e.printStackTrace();
        }

        return projects;
    }

    // Put projects into database
    @SuppressWarnings("unused")
    private void populateDatabase() {
        try (Connection connection = DriverManager.getConnection(DB_PATH); Statement statement = connection.createStatement()) {
            Scanner scnr = new Scanner(new File(CSV_FILE), StandardCharsets.UTF_8);

            // Consume header row
            scnr.nextLine();

            while (scnr.hasNext()) {
                String line = scnr.nextLine();

                Scanner row = new Scanner(line);
                row.useDelimiter(",");

                String projectTitle = row.next();
                String projectLocation = row.next();
                String projectDay = row.next();
                double hourlyValue = row.nextDouble();
                int registeredSlots = row.nextInt();
                int totalSlots = row.nextInt();

                String query = String.format("INSERT INTO project (project_title, project_location, project_day, hourly_value, registered_slots, total_slots, enabled) VALUES ('%s', '%s', '%s', %f, %d, %d, %d)", projectTitle, projectLocation, projectDay, hourlyValue, registeredSlots, totalSlots, 1);
                statement.executeUpdate(query);

                row.close();
            }

            scnr.close();
        } catch (IOException | SQLException e) {
            System.out.println("Error in ProcessCSV.populateDatabase: " + e.getClass().getName());
            e.printStackTrace();
        }
    }
}