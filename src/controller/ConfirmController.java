package controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Participation;
import model.Registration;
import services.CartServiceImpl;

public class ConfirmController {
    
    // Dependencies
    private final CartServiceImpl cartService;

    @FXML private Label contributionValue;
    @FXML private TextField codeInput;
    @FXML private Label resultLabel;
    
    public ConfirmController(CartServiceImpl cartService) {
        this.cartService = cartService;
    }

    @FXML // Show the total money earned and how many projects the user registered for
    public void initialize() {
        // Display total money earned from registrations
        double contributionSum = 0.0;

        for (Registration registration : this.cartService.getCart().values()) {
            contributionSum += (registration.getSlotOneHours() + registration.getSlotTwoHours() + registration.getSlotThreeHours()) * registration.getProject().getHourlyValue();
        }

        int numProjects = this.cartService.getCart().keySet().size();
        contributionValue.setText(String.format("You earned a total of $%.2f from %d %s", contributionSum, numProjects, numProjects == 1 ? "project" : "projects"));
    }

    @FXML
    public void handleConfirmClick() {
        try {
            // Ensure that input code is valid length
            if (codeInput.getLength() != 6) {
                resultLabel.setText("Invalid code");

                return;
            }

            // Ensure that code comprises only numbers
            Integer.parseInt(codeInput.getText());
            resultLabel.setText("");

            // Once code inputted, create participations
            ArrayList<Participation> participations = new ArrayList<Participation>();
            for (Registration registration : this.cartService.getCart().values()) {
                // Random four-digit ID was chosen instead of incremental IDs (i.e. 0001, 0002, etc.) as there's fewer chances of collisions and we don't have to keep track of what the previous number was in the sequence
                int participationID = (int) ((Math.random() * (10000 - 1000)) + 1000);

                // Use 24 hour time
                LocalDateTime myDateObj = LocalDateTime.now();
                DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                String participationDate = myDateObj.format(myFormatObj);

                // A slot is considered registered for if it contains a positive value
                int numSlots = 0;
                numSlots += registration.getSlotOneHours() > 0 ? 1 : 0;
                numSlots += registration.getSlotTwoHours() > 0 ? 1 : 0;
                numSlots += registration.getSlotThreeHours() > 0 ? 1 : 0;

                int totalHours = registration.getSlotOneHours() + registration.getSlotTwoHours() + registration.getSlotThreeHours();

                double totalContribution = totalHours * registration.getProject().getHourlyValue();

                participations.add(new Participation(participationID,
                                                     registration.getProject(),
                                                     registration.getUsername(),
                                                     participationDate,
                                                     numSlots,
                                                     totalHours,
                                                     totalContribution
                ));
            }

            // After cart has checked out, empty it and return to dashboard
            this.cartService.checkoutCart(participations);
            this.cartService.emptyCart();

            Stage stage = (Stage) codeInput.getScene().getWindow();
            stage.close();
        } catch (NumberFormatException e) {
            // System.out.println("Error in ConfirmController.handleConfirmClick: " + e.getClass().getName());
            // e.printStackTrace();

            resultLabel.setText("Invalid code");

            return;
        }
    }
}
