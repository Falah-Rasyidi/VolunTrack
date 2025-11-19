package controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.Project;
import model.User;
import services.CartServiceImpl;

public class ProjectController {
    
    // Dependencies
    private final Project project;
    private final User user;
    private final CartServiceImpl cartService;

    @FXML private Label projectTitle;
    @FXML private Label projectLocation;
    @FXML private Label projectDay;
    @FXML private Label hourlyRate;
    @FXML private Label registeredUsers;

    public ProjectController(Project project, User user, CartServiceImpl cartService) {
        this.project = project;
        this.user = user;
        this.cartService = cartService;
    }

    @FXML // Inject project information
    public void initialize() {
        projectTitle.setText(this.project.getProjectTitle());
        projectLocation.setText("Location: " + this.project.getProjectLocation());
        projectDay.setText("Day: " + project.getProjectDay());
        hourlyRate.setText(String.format("Hourly rate: $%.2f", project.getHourlyValue()));
        registeredUsers.setText("Registered slots: " + project.getRegisteredSlots() + "/" + project.getTotalSlots());
    }

    @FXML // Open interest scene in a new window
    public void openInterest() {
        try {
            // Inject project, user, and cart service into controller
            RegisterController controller = new RegisterController(this.project, this.user, this.cartService);

            // Set controller
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/register.fxml"));
            loader.setControllerFactory(c -> controller);
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Register");

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            System.out.println("Error in DashboardController.handleCartButtonClick: " + e.getClass().getName());
            e.printStackTrace();
        }
    }
}
