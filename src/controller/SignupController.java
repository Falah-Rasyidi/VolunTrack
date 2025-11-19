package controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import database.ProcessCSV;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Project;
import model.User;
import model.UserType;
import services.CartServiceImpl;
import services.UserServiceImpl;

public class SignupController {

    private Scene previousScene;

    @FXML private TextField fullname;
    @FXML private TextField username;
    @FXML private TextField email;
    @FXML private PasswordField password;
    @FXML private Label resultLabel;

    @FXML
    private void handleSignupClick(ActionEvent event) {
        Node eventSource = (Node) event.getSource();
        Stage stage = (Stage) eventSource.getScene().getWindow();

        try {
            UserServiceImpl service = new UserServiceImpl();
            Optional<User> query = service.findUser(username.getText());

            // If able to find matching username, input username is NOT unique
            if (query.isPresent()) {
                resultLabel.setText("Username already taken");

                return;
            }

            // Confirm that password conforms to requirments
            Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
            Matcher matcher = pattern.matcher(password.getText());
            if (!matcher.find()) {
                resultLabel.setText("Please ensure your password is at least 8 characters long, includes both letters and numbers and contains at least one uppercase letter and one special character");
                resultLabel.setWrapText(true);

                return;
            }

            resultLabel.setText("");
            
            // If all requirements met, add user to db and proceed to dashboard page
            // Encrypt password first
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String passwordHash = HexFormat.of().formatHex(digest.digest(password.getText().getBytes(StandardCharsets.UTF_8)));

            User newUser = new User(fullname.getText(), username.getText(), email.getText(), passwordHash, UserType.USER.toString());
            service.addUser(newUser);

            // Create projects and current user dependencies
            ArrayList<Project> projects = new ProcessCSV().loadPropertyData();
            User currentUser = newUser;
            
            // Create cart dependency
            CartServiceImpl cartService = new CartServiceImpl();
            cartService.setCart(newUser.getUsername());

            // Inject
            DashboardController controller = new DashboardController(projects, currentUser, cartService);

            // Set controller
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/dashboard.fxml"));
            loader.setControllerFactory(c -> controller);
            Parent root = loader.load();

            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (IOException | NoSuchAlgorithmException e) {
            System.out.println("Error in SignupController.handleSignupClick: " + e.getClass().getName());
            e.printStackTrace();
        }
    }

    public void handleLoginChangeClick(ActionEvent event) {
        Node eventSource = (Node) event.getSource();
        Stage stage = (Stage) eventSource.getScene().getWindow();

        stage.setScene(this.previousScene);
    }

    // helper method. previousScene should be the login page
    public void setPreviousScene(Scene scene) {
        this.previousScene = scene;
    }
}
