package services;

import java.util.ArrayList;

import javafx.collections.ObservableMap;
import model.Project;

public interface ProjectService {
    /**
     * updateAllProjects is only used to update the status of all projects upon admin logout.
     * Multiple project information can't be updated simultaneously, only one at a time.
     * Use the addProject method to update a project's details.
     */
    public void updateAllProjects(ObservableMap<String, ArrayList<Project>> projects);
    
    // Add project
    public void addProject(String projectTitle, String projectLocation, String projectDay, double hourlyRate, int totalSlots);

    // Modify existing project
    public void modifyProject(Project project);

    // Check if project already exists
    public boolean findDuplicateProject(int projectID, String projectTitle, String projectLocation, String projectDay);
}
