package controller;

import java.io.IOException;

import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
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
import model.Registration;
import model.User;
import services.CartServiceImpl;

public class CartController {

    // Dependencies
    private final User currentUser;
    private final CartServiceImpl cartService;

    private Scene previousScene;

    @FXML private Label welcomeMessage;
    @FXML private ScrollPane dashboardScroll;
    @FXML private FlowPane dashboardFlow;
    @FXML private Label resultLabel;

    public CartController(User currentUser, CartServiceImpl cartService) {
        this.currentUser = currentUser;
        this.cartService = cartService;
    }

    @FXML // refreshCart and listener help UI change when deleting a registration from the cart
    public void initialize() {
        welcomeMessage.setText("Welcome, " + this.currentUser.getUsername() + "!");
        ObservableMap<Integer, Registration> cart = this.cartService.getCart();

        refreshCart(cart);

        cart.addListener((MapChangeListener<Integer, Registration>) change -> {
            if (change.wasRemoved()) {
                refreshCart(cart);
            }
        });

        dashboardScroll.setContent(dashboardFlow);
    }

    // Update scene body when cart entry deleted
    private void refreshCart(ObservableMap<Integer, Registration> cart) {
        dashboardFlow.getChildren().clear();

        for (Registration registration : cart.values()) {
            // Only show cart entries that have enabled projects
            boolean projectEnabled = registration.getProject().getEnabled();
            if (projectEnabled) {
                try {
                    RegistrationController controller = new RegistrationController(registration, this.cartService);

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/registration.fxml"));
                    loader.setControllerFactory(c -> controller);
                    VBox projectCard = loader.load();

                    dashboardFlow.getChildren().add(projectCard);
                } catch (IOException e) {
                    System.out.println("Error in CartController.refreshCart: " + e.getClass().getName());
                    e.printStackTrace();
                }
            }
        }
    }
    
    @FXML // Return to login page after log out
    public void handleLogoutClick(ActionEvent event) {
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
            System.out.println("Error in CartController.handleLogoutClick: " + e.getClass().getName());
            e.printStackTrace();
        }
    }

    @FXML // Open password change scene in new window
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
            System.out.println("Error in CartController.openPasswordChange: " + e.getClass().getName());
            e.printStackTrace();
        }
    }

    @FXML // This hopefully shouldn't break because there's no other way to get to the cart page except from the dashboard
    public void handleDashboardClick(ActionEvent event) {
        Node eventSource = (Node) event.getSource();
        Stage stage = (Stage) eventSource.getScene().getWindow();

        stage.setScene(this.previousScene);
    }

    @FXML // Open export scene in a new window
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
    
    @FXML // Open participation history in a new window
    public void handleViewHistoryClick() {
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

    @FXML // Open confirmation in a new window
    public void openConfirmation() {
        // Ensure cart isn't empty
        if (this.cartService.getCart().isEmpty()) {
            resultLabel.setText("Add registrations to continue");

            return;
        }

        // Check if there's any registrations with zero hours
        for (Registration registration : this.cartService.getCart().values()) {
            if (registration.getSlotOneHours() == 0 && registration.getSlotTwoHours() == 0 && registration.getSlotThreeHours() == 0) {
                resultLabel.setText("There are registrations with no selected hours. Please select a minimum of one hour (in any slot)");

                return;
            }
        }

        resultLabel.setText("");
        try {
            // Inject cart service into controller
            ConfirmController controller = new ConfirmController(this.cartService);

            // Set controller
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/confirm.fxml"));
            loader.setControllerFactory(c -> controller);
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Confirm registrations");
            
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            System.out.println("Error in CartController.openConfirmation: " + e.getClass().getName());
            e.printStackTrace();
        }
    }

    // helper method. previousScene should always (ideally) be referring to the dashboard
    public void setPreviousScene(Scene scene) {
        this.previousScene = scene;
    }
}
