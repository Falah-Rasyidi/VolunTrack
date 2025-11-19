package controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import model.Project;
import services.ProjectServiceImpl;

public class ProjectAdminController {

    // Dependencies
    private final Project project;

    @FXML private Label projectTitle;
    @FXML private Label statusLabel;
    @FXML private Label projectLocation;
    @FXML private Label projectDay;
    @FXML private Label hourlyRate;
    @FXML private Label registeredUsers;
    @FXML private Button statusButton;

    public ProjectAdminController(Project project) {
        this.project = project;
    }

    @FXML // Set project information
    public void initialize() {
        projectTitle.setText(this.project.getProjectTitle());
        statusLabel.setText(this.project.getEnabled() ? "ENABLED" : "DISABLED");
        statusLabel.setTextFill(this.project.getEnabled() ? Paint.valueOf("#60ae00") : Paint.valueOf("#ff0f0f"));

        projectLocation.setText("Location: " + this.project.getProjectLocation());
        projectDay.setText("Day: " + project.getProjectDay());
        hourlyRate.setText(String.format("Hourly rate: $%.2f", project.getHourlyValue()));
        registeredUsers.setText("Registered slots: " + project.getRegisteredSlots() + "/" + project.getTotalSlots());

        statusButton.setText(this.project.getEnabled() ? "Disable" : "Enable");
    }

    @FXML // Open modify project scene in a new window
    public void openModifyProject() {
        try {
            // Create project service dependency
            ProjectServiceImpl projectService = new ProjectServiceImpl();

            // Inject dependencies into controller
            ProjectModifyController controller = new ProjectModifyController(this.project, projectService);

            // Set controller
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/project_modify.fxml"));
            loader.setControllerFactory(c -> controller);
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Modify project");
            
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            System.out.println("Error in ProjectAdminController.openModify: " + e.getClass().getName());
            e.printStackTrace();
        }
    }

    @FXML // If set to false, project won't appear on user dashboard
    public void changeStatus() {
        if (this.project.getEnabled()) {
            this.project.setEnabled(false);
            statusLabel.setText("DISABLED");
            statusLabel.setTextFill(Paint.valueOf("#ff0f0f"));
            statusButton.setText("Enable");
        }
        else {
            this.project.setEnabled(true);
            statusLabel.setText("ENABLED");
            statusLabel.setTextFill(Paint.valueOf("#60ae00"));
            statusButton.setText("Disable");
        }
    }
}
