package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import model.Project;
import model.Registration;
import services.CartServiceImpl;

public class RegistrationController {
    
    // Dependencies
    private final Registration registration;
    private final CartServiceImpl cartService;

    @FXML private Label projectTitle;
    @FXML private Label projectLocation;
    @FXML private Label projectDay;
    @FXML private Label hourlyRate;
    @FXML private ComboBox<Integer> slotOneHours;
    @FXML private ComboBox<Integer> slotTwoHours;
    @FXML private ComboBox<Integer> slotThreeHours;
    @FXML private Label slotOneLabel;
    @FXML private Label slotTwoLabel;
    @FXML private Label slotThreeLabel;

    public RegistrationController(Registration registration, CartServiceImpl cartService) {
        this.registration = registration;
        this.cartService = cartService;
    }

    @FXML // Inject project information into registration
    public void initialize() {
        Project project = this.registration.getProject();
        projectTitle.setText(project.getProjectTitle());
        projectLocation.setText("Location: " + project.getProjectLocation());
        projectDay.setText("Day: " + project.getProjectDay());
        hourlyRate.setText(String.format("Rate: $%.2f", project.getHourlyValue()));

        slotOneHours.getItems().addAll(0, 1, 2, 3);
        slotOneHours.setValue(this.registration.getSlotOneHours());

        slotTwoHours.getItems().addAll(0, 1, 2, 3);
        slotTwoHours.setValue(this.registration.getSlotTwoHours());

        slotThreeHours.getItems().addAll(0, 1, 2, 3);
        slotThreeHours.setValue(this.registration.getSlotThreeHours());

        handleSlotChange();
    }

    @FXML // Cart UI should update registration is deleted from cart
    public void handleDeleteClick(ActionEvent event) {
        cartService.deleteRegistration(this.registration.getProject().getProjectID());
    }

    @FXML
    public void handleSlotChange() {
        slotOneLabel.setText(slotOneHours.getValue() == 1 ? "hour" : "hours");
        slotTwoLabel.setText(slotTwoHours.getValue() == 1 ? "hour" : "hours");
        slotThreeLabel.setText(slotThreeHours.getValue() == 1 ? "hour" : "hours");

        this.registration.setSlotOneHours(slotOneHours.getValue());
        this.registration.setSlotTwoHours(slotTwoHours.getValue());
        this.registration.setSlotThreeHours(slotThreeHours.getValue());
    }
}
