package services;

import javafx.collections.ObservableList;
import model.Participation;

public interface ParticipationHistoryService {
    // Retrieve the user's participations
    public ObservableList<Participation> getParticipations(String username);

    // Retrieve all user's participations
    public ObservableList<Participation> getAllParticipations();
}
