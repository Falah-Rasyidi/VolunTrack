package services;

import javafx.collections.ObservableList;
import model.Participation;
import repositories.ParticipationHistoryRepositoryImpl;

public class ParticipationHistoryServiceImpl implements ParticipationHistoryService {
    @Override
    public ObservableList<Participation> getParticipations(String username) {
        return new ParticipationHistoryRepositoryImpl().getParticipations(username);
    }

    @Override
    public ObservableList<Participation> getAllParticipations() {
        return new ParticipationHistoryRepositoryImpl().getAllParticipations();
    }
}
