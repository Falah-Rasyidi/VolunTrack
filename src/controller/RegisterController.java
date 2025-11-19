package controller;

import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.Project;
import model.Registration;
import model.User;
import services.CartServiceImpl;

public class RegisterController {
    
    // Dependencies
    private final Project project;
    private final User user;
    private final CartServiceImpl cartService;

    @FXML private Label registrationTitle;
    @FXML private Label registrationLocation;
    @FXML private Label registrationDay;
    @FXML private Label registrationRate;
    @FXML private Label registrationAvailSlots;
    @FXML private ComboBox<Integer> slotOneHours;
    @FXML private ComboBox<Integer> slotTwoHours;
    @FXML private ComboBox<Integer> slotThreeHours;
    @FXML private Label slotOneLabel;
    @FXML private Label slotTwoLabel;
    @FXML private Label slotThreeLabel;
    @FXML private Label resultLabel;
    @FXML private Button addToCart;

    public RegisterController(Project project, User user, CartServiceImpl cartService) {
        this.project = project;
        this.user = user;
        this.cartService = cartService;
    }

    @FXML
    public void initialize() {
        registrationTitle.setText(project.getProjectTitle());
        registrationLocation.setText("Location: " + project.getProjectLocation());
        registrationDay.setText("Day: " + project.getProjectDay());
        registrationRate.setText(String.format("Rate: $%.2f", project.getHourlyValue()));
        registrationAvailSlots.setText("Available slots: " + project.getAvailableSlots());

        // Get slot hours for the project (if it exists)
        ObservableMap<Integer, Registration> cart = this.cartService.getCart();

        slotOneHours.getItems().addAll(0, 1, 2, 3);
        slotOneHours.setValue(cart.containsKey(this.project.getProjectID()) ? cart.get(this.project.getProjectID()).getSlotOneHours() : 0);

        slotTwoHours.getItems().addAll(0, 1, 2, 3);
        slotTwoHours.setValue(cart.containsKey(this.project.getProjectID()) ? cart.get(this.project.getProjectID()).getSlotTwoHours() : 0);

        slotThreeHours.getItems().addAll(0, 1, 2, 3);
        slotThreeHours.setValue(cart.containsKey(this.project.getProjectID()) ? cart.get(this.project.getProjectID()).getSlotThreeHours() : 0);

        handleSlotChange();
    }

    @FXML
    public void handleAddToCartClick() {
        // Ensure at least one of the hours is > 0
        if (slotOneHours.getValue() == 0 && slotTwoHours.getValue() == 0 && slotThreeHours.getValue() == 0) {
            resultLabel.setText("Please select a minimum of one hour (in any slot)");

            return;
        }

        resultLabel.setText("");

        this.cartService.addToCart(new Registration(this.project, this.user.getUsername(), slotOneHours.getValue(), slotTwoHours.getValue(), slotThreeHours.getValue()));

        // Close window after adding to cart
        Stage stage = (Stage) addToCart.getScene().getWindow();
        stage.close();
    }

    @FXML // Change plurality of hour depending on whether hour is set to 1 or not
    public void handleSlotChange() {
        slotOneLabel.setText(slotOneHours.getValue() == 1 ? "hour" : "hours");
        slotTwoLabel.setText(slotTwoHours.getValue() == 1 ? "hour" : "hours");
        slotThreeLabel.setText(slotThreeHours.getValue() == 1 ? "hour" : "hours");
    }
}
