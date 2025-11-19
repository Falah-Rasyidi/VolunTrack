package services;

import java.util.ArrayList;

import javafx.collections.ObservableMap;
import model.Participation;
import model.Registration;

public interface CartService {
    // Get user's cart from database and save it 
    public void setCart(String username);

    // Retrieve user's saved cart
    public ObservableMap<Integer, Registration> getCart();

    // Add registration to cart
    public void addToCart(Registration registration);

    // Save participations in cart to database
    public void checkoutCart(ArrayList<Participation> cart);

    // Save registrations in cart in case user logs out
    public void saveCart(ArrayList<Registration> cart);

    // Remove registration from cart
    public void deleteRegistration(int projectID);

    // To be called after checking out
    public void emptyCart();
}
