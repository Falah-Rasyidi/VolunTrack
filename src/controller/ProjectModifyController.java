package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.Project;
import services.ProjectServiceImpl;

public class ProjectModifyController {
    
    // Dependencies
    private final Project project;
    private final ProjectServiceImpl projectService;

    @FXML private TextField projectTitle;
    @FXML private TextField projectLocation;
    @FXML private ComboBox<String> projectDay;
    @FXML private TextField hourlyRate;
    @FXML private TextField totalSlots;
    @FXML private Label resultLabel;

    public ProjectModifyController(Project project, ProjectServiceImpl projectService) {
        this.project = project;
        this.projectService = projectService;
    }

    @FXML // Inject project information into text fields
    public void initialize() {
        projectTitle.setText(this.project.getProjectTitle());
        projectLocation.setText(this.project.getProjectLocation());
        projectDay.getItems().addAll("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun");
        projectDay.setValue(this.project.getProjectDay());
        hourlyRate.setText(String.valueOf(this.project.getHourlyValue()));
        totalSlots.setText(String.valueOf(this.project.getTotalSlots()));
    }

    @FXML
    public void handleUpdateClick(ActionEvent event) {
        // Ensure input fields meet requirements (project title / project location aren't empty or > 30 chars)
        if (projectTitle.getText().isBlank()) {
            resultLabel.setWrapText(true);
            resultLabel.setText("Please input a project title");

            return;
        }
        if (projectTitle.getText().length() > 30) {
            resultLabel.setWrapText(true);
            resultLabel.setText("Project titles cannot be longer than 30 characters");

            return;
        }
        
        if (projectLocation.getText().isBlank()) {
            resultLabel.setWrapText(true);
            resultLabel.setText("Please input a project location");

            return;
        }
        if (projectLocation.getText().length() > 30) {
            resultLabel.setWrapText(true);
            resultLabel.setText("Project location cannot be longer than 30 characters");

            return;
        }
        
        // Hourly rate and total slots should be between 1 and 100 inclusive
        double hourlyRateDouble = 0.0;
        int totalSlotsInt = 0;
        try {
            hourlyRateDouble = Double.parseDouble(hourlyRate.getText());
            if (hourlyRateDouble < 1 || hourlyRateDouble > 100) {
                resultLabel.setWrapText(true);
                resultLabel.setText("Please input an hourly rate between $1 and $100");

                return;
            }

            totalSlotsInt = Integer.parseInt(totalSlots.getText());
            if (totalSlotsInt < 1 || totalSlotsInt > 100) {
                resultLabel.setWrapText(true);
                resultLabel.setText("Please input a total slot value between 1 and 100");

                return;
            }
        } catch (NumberFormatException e) {
            // System.out.println("Error in ProjectModifyController.handleUpdateClick: " + e.getClass().getName());
            // e.printStackTrace();

            resultLabel.setWrapText(true);
            resultLabel.setText("Please input numbers in the \"Hourly rate\" and \"Total slots\" fields");

            return;
        }

        // Check for duplicates
        if (this.projectService.findDuplicateProject(this.project.getProjectID(), projectTitle.getText(), projectLocation.getText(), projectDay.getValue())) {
            resultLabel.setWrapText(true);
            resultLabel.setText("A project with these details already exist");

            return;
        }

        // Everything is valid, update project
        this.projectService.modifyProject(new Project(this.project.getProjectID(),
                                                      projectTitle.getText(),
                                                      projectLocation.getText(),
                                                      projectDay.getValue(),
                                                      hourlyRateDouble,
                                                      this.project.getRegisteredSlots(),
                                                      totalSlotsInt,
                                                      this.project.getEnabled()
        ));

        resultLabel.setWrapText(true);
        resultLabel.setText("Project successfully modified! Please logout and login to see your changes");
    }
}
