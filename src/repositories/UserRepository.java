package repositories;

import java.util.List;
import java.util.Optional;

import model.User;

public interface UserRepository {
    // Retrieve list of all users
    public List<User> getUsers();

    // Add user to database
    public boolean addUser(User newUser);

    // Find if user already exists
    public Optional<User> findUser(String username);
}
