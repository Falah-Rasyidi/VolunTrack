package services;

import java.util.List;
import java.util.Optional;

import model.User;

public interface UserService {
    // Retrieve list of all users
    public List<User> getUsers();

    // Add user to database
    public boolean addUser(User newUser);

    // Find if user already exists
    public Optional<User> findUser(String username);
}
