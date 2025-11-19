package repositories;

import java.util.ArrayList;

import javafx.collections.ObservableMap;
import model.Participation;
import model.Registration;

public interface CartRepository {
    // Get user's cart from database
    public ObservableMap<Integer, Registration> retrieveCart(String username);

    // Save participations to db
    public void checkoutCart(ArrayList<Participation> cart);

    // Save registrations in cart if user logs out
    public void saveCart(ArrayList<Registration> cart);
}
