package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import services.ProjectServiceImpl;

public class ProjectAddController {
    
    // Dependencies
    private final ProjectServiceImpl projectService;

    @FXML private TextField projectTitle;
    @FXML private TextField projectLocation;
    @FXML private ComboBox<String> projectDay;
    @FXML private TextField hourlyRate;
    @FXML private TextField totalSlots;
    @FXML private Label resultLabel;

    public ProjectAddController(ProjectServiceImpl projectService) {
        this.projectService = projectService;
    }

    @FXML // Set values for dropdown menu and set default value to first one
    public void initialize() {
        projectDay.getItems().addAll("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun");
        projectDay.setValue("Mon");
    }

    @FXML
    public void handleUpdateClick(ActionEvent event) {
        // Ensure project title isn't empty
        if (projectTitle.getText().isBlank()) {
            resultLabel.setWrapText(true);
            resultLabel.setText("Please input a project title");

            return;
        }
        // Ensure project title length isn't > 30 chars
        if (projectTitle.getText().length() > 30) {
            resultLabel.setWrapText(true);
            resultLabel.setText("Project titles cannot be longer than 30 characters");

            return;
        }
        
        // Ensure project location isn't empty
        if (projectLocation.getText().isBlank()) {
            resultLabel.setWrapText(true);
            resultLabel.setText("Please input a project location");

            return;
        }
        // Ensure project location length isn't > 30 chars
        if (projectLocation.getText().length() > 30) {
            resultLabel.setWrapText(true);
            resultLabel.setText("Project location cannot be longer than 30 characters");

            return;
        }
        
        // Ensure hourly rate and total slots are between 1 and 100 inclusive
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
            // System.out.println("Error in ProjectAddController.handleUpdateClick: " + e.getClass().getName());
            // e.printStackTrace();

            resultLabel.setWrapText(true);
            resultLabel.setText("Please input numbers in the \"Hourly rate\" and \"Total slots\" fields");

            return;
        }

        // Check for duplicates
        if (this.projectService.findDuplicateProject(-1, projectTitle.getText(), projectLocation.getText(), projectDay.getValue())) {
            resultLabel.setWrapText(true);
            resultLabel.setText("A project with these details already exist");

            return;
        }

        // Everything is valid, update project
        this.projectService.addProject(projectTitle.getText(), projectLocation.getText(), projectDay.getValue(), hourlyRateDouble, totalSlotsInt);

        resultLabel.setWrapText(true);
        resultLabel.setText("Project successfully created! Please logout and login to see your changes");
    }
}
