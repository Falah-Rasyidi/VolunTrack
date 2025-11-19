package repositories;

public interface PasswordChangeRepository {
    /**
     * New password must meet following requirements:
     * - At least 8 characters long
     * - Include both letters and numbers
     * - Contain at least one uppercase letter and one special character (!, @, #, $, etc.)
     */
    public boolean changePassword(String username, String newPassword);
}
