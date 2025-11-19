package services;

import java.util.List;
import java.util.Optional;

import model.User;
import repositories.UserRepositoryImpl;

public class UserServiceImpl implements UserService {
    @Override
    public List<User> getUsers() {
        return new UserRepositoryImpl().getUsers();
    }

    @Override
    public boolean addUser(User newUser) {
        return new UserRepositoryImpl().addUser(newUser);
    }

    @Override
    public Optional<User> findUser(String username) {
        return new UserRepositoryImpl().findUser(username);
    }
}
