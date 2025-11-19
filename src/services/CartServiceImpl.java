package services;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import model.Participation;
import model.Registration;
import repositories.CartRepositoryImpl;

public class CartServiceImpl implements CartService {

    private ObservableMap<Integer, Registration> cart = FXCollections.observableHashMap();

    @Override
    public void setCart(String username) {
        this.cart = new CartRepositoryImpl().retrieveCart(username);
    }

    @Override
    public ObservableMap<Integer, Registration> getCart() {
        return this.cart;
    }

    @Override
    public void addToCart(Registration registration) {
        cart.put(registration.getProject().getProjectID(), registration);
    }

    @Override
    public void checkoutCart(ArrayList<Participation> cart) {
        new CartRepositoryImpl().checkoutCart(cart);
    }

    @Override
    public void saveCart(ArrayList<Registration> cart) {
        new CartRepositoryImpl().saveCart(cart);
    }

    @Override
    public void deleteRegistration(int projectID) {
        cart.remove(projectID);
    }

    @Override
    public void emptyCart() {
        cart.clear();
    }
}
