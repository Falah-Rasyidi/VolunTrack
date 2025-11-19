package services;

import repositories.PasswordChangeRepositoryImpl;

public class PasswordChangeServiceImpl implements PasswordChangeService {
    @Override
    public boolean changePassword(String username, String newPassword) {
        return new PasswordChangeRepositoryImpl().changePassword(username, newPassword);
    }
}
