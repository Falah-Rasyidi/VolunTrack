package controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.Optional;

import database.ProcessCSV;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Project;
import model.User;
import model.UserType;
import services.CartServiceImpl;
import services.ProjectServiceImpl;
import services.UserServiceImpl;

public class LoginController {
    
    private Scene nextScene;

    @FXML private TextField username;
    @FXML private PasswordField password;
    @FXML private Button login;
    @FXML private Button signupChange;
    @FXML private Label resultLabel;

    @FXML
    private void handleLoginClick(ActionEvent event) {
        Node eventSource = (Node) event.getSource();
        Stage stage = (Stage) eventSource.getScene().getWindow();

        try {
            UserServiceImpl service = new UserServiceImpl();
            Optional<User> query = service.findUser(username.getText());
            
            // Able to locate a user with given username
            if (!query.isPresent()) {
                resultLabel.setText("Could not find account with that username");

                return;
            }

            // Verify password
            User user = query.get();
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String passwordHash = HexFormat.of().formatHex(digest.digest(password.getText().getBytes(StandardCharsets.UTF_8)));
            if (!user.getPasswordHash().equals(passwordHash)) {
                resultLabel.setText("Password is incorrect");

                return;
            }

            resultLabel.setText("");
            
            // Create current user dependency (both users and admins need this)
            User currentUser = user;

            // Check if current user is an admin
            if (currentUser.getUserType().equals(UserType.ADMIN.toString())) {
                // Create project and project service dependencies
                ObservableMap<String, ArrayList<Project>> projects = new ProcessCSV().loadAdminPropertyData();
                ProjectServiceImpl projectService = new ProjectServiceImpl();

                // Inject
                DashboardAdminController controller = new DashboardAdminController(projects, currentUser, projectService);

                // Set controller
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/dashboard_admin.fxml"));
                loader.setControllerFactory(c -> controller);
                Parent root = loader.load();

                Scene scene = new Scene(root);
                stage.setScene(scene);
            }
            else {
                // Create project and cart dependencies
                ArrayList<Project> projects = new ProcessCSV().loadPropertyData();
                CartServiceImpl cartService = new CartServiceImpl();
                cartService.setCart(user.getUsername());

                // Inject
                DashboardController controller = new DashboardController(projects, currentUser, cartService);

                // Set controller
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/dashboard.fxml"));
                loader.setControllerFactory(c -> controller);
                Parent root = loader.load();

                Scene scene = new Scene(root);
                stage.setScene(scene);
            }
        } catch (IOException | NoSuchAlgorithmException e) {
            System.out.println("Error in LoginController.handleLoginClick: " + e.getClass().getName());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSignupChangeClick(ActionEvent event) {
        Node eventSource = (Node) event.getSource();
        Stage stage = (Stage) eventSource.getScene().getWindow();

        // Load sign up page if it hasn't already been loaded
        if (nextScene == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/signup.fxml"));
                Parent root = loader.load();

                SignupController controller = (SignupController) loader.getController();
                controller.setPreviousScene(eventSource.getScene());

                Scene scene = new Scene(root);
                setNextScene(scene);
                
                stage.setScene(scene);
            } catch (IOException e) {
                System.out.println("Error in LoginController.handleSignupChangeClick: " + e.getClass().getName());
                e.printStackTrace();
            }
        }
        else {
            stage.setScene(this.nextScene);
        }
    }

    public void setNextScene(Scene scene) {
        this.nextScene = scene;
    }
}
