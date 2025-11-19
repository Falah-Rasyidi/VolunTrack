package repositories;

import java.util.ArrayList;

import javafx.collections.ObservableMap;
import model.Project;

public interface ProjectRepository {
    // Update status of all projects
    public void updateAllProjects(ObservableMap<String, ArrayList<Project>> projects);

    // Add project
    public void addProject(String projectTitle, String projectLocation, String projectDay, double hourlyRate, int totalSlots);

    // Modify existing project
    public void modifyProject(Project project);

    // Check if project already exists
    public boolean findDuplicateProject(int projectID, String projectTitle, String projectLocation, String projectDay);
}
