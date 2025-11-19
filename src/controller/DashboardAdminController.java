package controller;

import java.io.IOException;
import java.util.ArrayList;

import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Project;
import model.User;
import services.ProjectServiceImpl;

public class DashboardAdminController {
    
    // Dependencies
    private final ObservableMap<String, ArrayList<Project>> projects;
    private final User currentUser;
    private final ProjectServiceImpl projectService;
    
    @FXML private Label welcomeMessage;
    @FXML private ScrollPane dashboardScroll;
    @FXML private FlowPane dashboardFlow;

    public DashboardAdminController(ObservableMap<String, ArrayList<Project>> projects, User currentUser, ProjectServiceImpl projectService) {
        this.projects = projects;
        this.currentUser = currentUser;
        this.projectService = projectService;
    }

    @FXML
    public void initialize() {
        welcomeMessage.setText("Welcome, " + this.currentUser.getUsername() + " (Admin)");

        // For each project group, create a tab pane
        for (ArrayList<Project> projectList : this.projects.values()) {
            try {
                FXMLLoader groupLoader = new FXMLLoader(getClass().getResource("../view/project_group.fxml"));
                TabPane projectGroup = groupLoader.load();

                // For each project in the group, create a new tab in the pane
                for (Project project : projectList) {
                    ProjectAdminController controller = new ProjectAdminController(project);

                    FXMLLoader projectLoader = new FXMLLoader(getClass().getResource("../view/project_admin.fxml"));
                    projectLoader.setControllerFactory(c -> controller);
                    VBox projectCard = projectLoader.load();

                    projectGroup.getTabs().add(new Tab(project.getProjectDay(), projectCard));
                }

                dashboardFlow.getChildren().add(projectGroup);
            } catch (IOException e) {
                System.out.println("Error in DashboardAdminController.initialize: " + e.getClass().getName());
                e.printStackTrace();
            }
        }

        dashboardScroll.setContent(dashboardFlow);
    }

    @FXML // Go to login page after log out
    public void handleLogoutClick(ActionEvent event) {
        // Update (if necessary) all projects in database
        this.projectService.updateAllProjects(this.projects);

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
            System.out.println("Error in DashboardAdminController.handleLogoutClick: " + e.getClass().getName());
            e.printStackTrace();
        }
    }

    @FXML // Opens add project scene in a new window
    public void openAddProject() {
        try {
            // Create project service dependency
            ProjectServiceImpl projectService = new ProjectServiceImpl();

            // Inject dependencies into controller
            ProjectAddController controller = new ProjectAddController(projectService);

            // Set controller
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/project_add.fxml"));
            loader.setControllerFactory(c -> controller);
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Add project");
            
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            System.out.println("Error in ProjectAdminController.openAddProject: " + e.getClass().getName());
            e.printStackTrace();
        }
    }

    @FXML // Opens password change scene in a new window
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
            System.out.println("Error in DashboardAdminController.openPasswordChange: " + e.getClass().getName());
            e.printStackTrace();
        }
    }

    @FXML // Open export history scene in a new window
    public void handleExportHistoryClick() {
        try {
            // Inject user into controller
            ExportHistoryController controller = new ExportHistoryController(this.currentUser);

            // Set controller
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/export_history_admin.fxml"));
            loader.setControllerFactory(c -> controller);
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Export history");
            
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            System.out.println("Error in DashboardAdminController.handleExportHistoryClick: " + e.getClass().getName());
            e.printStackTrace();
        }
    }

    @FXML // Open view history scene in a new window
    public void handleViewHistoryClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/view_history_admin.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Participation history");
            
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            System.out.println("Error in DashboardAdminController.handleViewHistoryClick: " + e.getClass().getName());
            e.printStackTrace();
        }
    }
}
