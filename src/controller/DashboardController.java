package controller;

import java.io.IOException;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Project;
import model.Registration;
import model.User;
import services.CartServiceImpl;

public class DashboardController {

    // Dependencies
    private final ArrayList<Project> projects;
    private final User currentUser;
    private final CartServiceImpl cartService;
    
    @FXML private Label welcomeMessage;
    @FXML private ScrollPane dashboardScroll;
    @FXML private FlowPane dashboardFlow;

    public DashboardController(ArrayList<Project> projects, User currentUser, CartServiceImpl cartService) {
        this.projects = projects;
        this.currentUser = currentUser;
        this.cartService = cartService;
    }

    @FXML
    public void initialize() {
        welcomeMessage.setText("Welcome, " + this.currentUser.getUsername() + "!");

        // For each project, create a new project view and inject project info into it
        for (Project project : this.projects) {
            boolean projectEnabled = project.getEnabled();
            if (projectEnabled) {
                try {
                    ProjectController controller = new ProjectController(project, this.currentUser, this.cartService);

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/project.fxml"));
                    loader.setControllerFactory(c -> controller);
                    VBox projectCard = loader.load();

                    dashboardFlow.getChildren().add(projectCard);
                } catch (IOException e) {
                    System.out.println("Error in DashboardController.initialize: " + e.getClass().getName());
                    e.printStackTrace();
                }
            }
        }

        dashboardScroll.setContent(dashboardFlow);
    }

    @FXML // Go to login page upon log out
    public void handleLogoutClick(ActionEvent event) {
        // Save any registrations to db before logging out
        if (!this.cartService.getCart().isEmpty()) {
            ArrayList<Registration> cart = new ArrayList<Registration>();
            for (Registration registration : this.cartService.getCart().values()) {
                cart.add(registration);
            }
            this.cartService.saveCart(cart);
        }

        Node eventSource = (Node) event.getSource();
        Stage stage = (Stage) eventSource.getScene().getWindow();

        try {
            LoginController controller = new LoginController();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/login.fxml"));
            loader.setControllerFactory(c -> controller);
            Parent root = loader.load();

            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (IOException e) {
            System.out.println("Error in DashboardController.handleLogoutClick: " + e.getClass().getName());
            e.printStackTrace();
        }
    }

    @FXML // Open password change scene in a new window
    public void openPasswordChange(ActionEvent event) {
        try {
            // Inject user into controller
            ChangePasswordController controller = new ChangePasswordController(this.currentUser);

            // Set controller
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/change_password.fxml"));
            loader.setControllerFactory(c -> controller);
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Change password");
            
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            System.out.println("Error in DashboardController.openPasswordChange: " + e.getClass().getName());
            e.printStackTrace();
        }
    }

    @FXML // Cart page a new scene with the same nav bar as the dasboard. Should look seamless
    public void openCart(ActionEvent event) {
        Node eventSource = (Node) event.getSource();
        Stage stage = (Stage) eventSource.getScene().getWindow();

        try {
            // Inject user and cart service dependencies
            CartController controller = new CartController(this.currentUser, this.cartService);

            // Set controller
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/cart.fxml"));
            loader.setControllerFactory(c -> controller);
            Parent root = loader.load();

            // Make dashboard the cart's previous scene
            controller.setPreviousScene(eventSource.getScene());

            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (IOException e) {
            System.out.println("Error in DashboardController.openCart: " + e.getClass().getName());
            e.printStackTrace();
        }
    }

    @FXML // Open export history scene in a new window
    public void handleExportHistoryClick() {
        try {
            // Inject user into controller
            ExportHistoryController controller = new ExportHistoryController(this.currentUser);

            // Set controller
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/export_history.fxml"));
            loader.setControllerFactory(c -> controller);
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Export history");
            
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            System.out.println("Error in DashboardController.handleExportHistoryClick: " + e.getClass().getName());
            e.printStackTrace();
        }
    }

    @FXML // Open view history scene in a new window
    public void handleViewHistoryClick(ActionEvent event) {
        try {
            // Inject user into controller
            ViewHistoryController controller = new ViewHistoryController(this.currentUser);

            // Set controller
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/view_history.fxml"));
            loader.setControllerFactory(c -> controller);
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Participation history");
            
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            System.out.println("Error in DashboardController.handleViewHistoryClick: " + e.getClass().getName());
            e.printStackTrace();
        }
    }
}
