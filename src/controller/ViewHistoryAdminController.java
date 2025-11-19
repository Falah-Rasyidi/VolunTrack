package controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Participation;
import services.ParticipationHistoryServiceImpl;

public class ViewHistoryAdminController {
    
    @FXML private TableView<Participation> participationTable;
    @FXML private TableColumn<Participation, Integer> registrationID;
    @FXML private TableColumn<Participation, String> username;
    @FXML private TableColumn<Participation, String> registrationDate;
    @FXML private TableColumn<Participation, String> projectTitle;
    @FXML private TableColumn<Participation, String> projectLocation;
    @FXML private TableColumn<Participation, String> projectDay;
    @FXML private TableColumn<Participation, Integer> registeredSlots;
    @FXML private TableColumn<Participation, Integer> totalHours;
    @FXML private TableColumn<Participation, Double> totalValue;

    @FXML
    public void initialize() {
        // Get user's participation history
        ObservableList<Participation> participations = new ParticipationHistoryServiceImpl().getAllParticipations();

        // Bind Participation attributes to columns
        registrationID.setCellValueFactory(new PropertyValueFactory<>("participationID"));
        username.setCellValueFactory(new PropertyValueFactory<>("username"));
        registrationDate.setCellValueFactory(new PropertyValueFactory<>("participationDate"));
        projectTitle.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getProject().getProjectTitle()));
        projectLocation.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getProject().getProjectLocation()));
        projectDay.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getProject().getProjectDay()));
        registeredSlots.setCellValueFactory(new PropertyValueFactory<>("numSlots"));
        totalHours.setCellValueFactory(new PropertyValueFactory<>("totalHours"));
        totalValue.setCellValueFactory(new PropertyValueFactory<>("totalContribution"));

        // Reverse list so that it's in reverse chronological order
        participationTable.setItems(participations);
        registrationDate.setSortType(TableColumn.SortType.DESCENDING);
        participationTable.getSortOrder().add(registrationDate);
        participationTable.sort();
    }
}
