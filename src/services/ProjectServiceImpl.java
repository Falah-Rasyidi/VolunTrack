package services;

import java.util.ArrayList;

import javafx.collections.ObservableMap;
import model.Project;
import repositories.ProjectRepositoryImpl;

public class ProjectServiceImpl implements ProjectService {
    @Override
    public void updateAllProjects(ObservableMap<String, ArrayList<Project>> projects) {
        new ProjectRepositoryImpl().updateAllProjects(projects);
    }

    @Override
    public void addProject(String projectTitle, String projectLocation, String projectDay, double hourlyRate, int totalSlots) {
        new ProjectRepositoryImpl().addProject(projectTitle, projectLocation, projectDay, hourlyRate, totalSlots);
    }

    @Override
    public void modifyProject(Project project) {
        new ProjectRepositoryImpl().modifyProject(project);
    }

    @Override
    public boolean findDuplicateProject(int projectID, String projectTitle, String projectLocation, String projectDay) {
        return new ProjectRepositoryImpl().findDuplicateProject(projectID, projectTitle, projectLocation, projectDay);
    }
}
