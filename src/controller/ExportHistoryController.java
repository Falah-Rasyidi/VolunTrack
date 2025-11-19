package controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import model.Participation;
import model.User;
import services.ParticipationHistoryServiceImpl;

// This controller is used by both the user and admin export history view
public class ExportHistoryController {
    
    // Dependencies
    private final User user;

    @FXML private Label resultLabel;

    public ExportHistoryController(User user) {
        this.user = user;
    }

    @FXML // Open export scene in a new window
    public void handleExportClick() {
        // Get user's history of participations in reverse chronological order
        ObservableList<Participation> participations = new ParticipationHistoryServiceImpl().getParticipations(this.user.getUsername());

        // Sort dates in reverse chronological order
        participations.sort((a, b) -> { return -1 * a.getParticipationDate().compareTo(b.getParticipationDate()); });

        try {
            // Get file and clear it before outputting to it
            File participationHistory = new File("participation_history.txt");
            new PrintWriter(participationHistory).close();

            FileWriter writer = new FileWriter(participationHistory, true);
            writer.write(String.format(" %-16s | %-19s | %-30s | %-20s | %-3s | %-7s | %-11s | %-6s%n", "Participation ID", "Register date", "Project title", "Project location", "Day", "# slots", "Total hours", "Total value"));
            writer.write(new String(new char[140]).replace('\u0000', '-') + '\n');
            for (Participation participation : participations) {
                writer.write(String.format(" %-16d | %-19s | %-30s | %-20s | %-3s | %-7d | %-11d | $%.2f%n", participation.getParticipationID(), participation.getParticipationDate(), participation.getProject().getProjectTitle(), participation.getProject().getProjectLocation(), participation.getProject().getProjectDay(), participation.getNumSlots(), participation.getTotalHours(), participation.getTotalContribution() + '\n'));
            }
            writer.close();

            resultLabel.setText("Participation history successfully exported!");
        } catch (IOException e) {
            System.out.println("Error in ExportHistoryController.handleExportClick: " + e.getClass().getName());
            e.printStackTrace();
        }
    }

    @FXML // Open admin export scene in a new window
    public void handleAdminExportClick() {
        // Get all user's history of participations in reverse chronological order
        ObservableList<Participation> participations = new ParticipationHistoryServiceImpl().getAllParticipations();

        // Sort dates in reverse chronological order
        participations.sort((a, b) -> { return -1 * a.getParticipationDate().compareTo(b.getParticipationDate()); });

        try {
            // Get file and clear it before outputting to it
            File participationHistory = new File("participation_history_admin.txt");
            new PrintWriter(participationHistory).close();

            FileWriter writer = new FileWriter(participationHistory, true);
            writer.write(String.format(" %-16s | %-8s | %-19s | %-30s | %-20s | %-3s | %-7s | %-11s | %-6s%n", "Participation ID", "Username", "Register date", "Project title", "Project location", "Day", "# slots", "Total hours", "Total value"));
            writer.write(new String(new char[151]).replace('\u0000', '-') + '\n');
            for (Participation participation : participations) {
                writer.write(String.format(" %-16d | %-8s | %-19s | %-30s | %-20s | %-3s | %-7d | %-11d | $%.2f%n", participation.getParticipationID(), participation.getUsername(), participation.getParticipationDate(), participation.getProject().getProjectTitle(), participation.getProject().getProjectLocation(), participation.getProject().getProjectDay(), participation.getNumSlots(), participation.getTotalHours(), participation.getTotalContribution() + '\n'));
            }
            writer.close();

            resultLabel.setText("Participation history successfully exported!");
        } catch (IOException e) {
            System.out.println("Error in ExportHistoryController.handleExportClick: " + e.getClass().getName());
            e.printStackTrace();
        }
    }
}
