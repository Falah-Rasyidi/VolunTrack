package controller;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import model.User;
import services.PasswordChangeServiceImpl;

public class ChangePasswordController {

    // Dependencies
    private final User user;
    
    @FXML private PasswordField oldPassword;
    @FXML private PasswordField newPassword;
    @FXML private PasswordField repeatPassword;
    @FXML private Label resultLabel;

    public ChangePasswordController(User user) {
        this.user = user;
    }

    @FXML // Open change password in a new window
    public void onChangePasswordClick() {
        try {
            // Verify old password
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String passwordHash = HexFormat.of().formatHex(digest.digest(oldPassword.getText().getBytes(StandardCharsets.UTF_8)));

            if (!user.getPasswordHash().equals(passwordHash)) {
                resultLabel.setText("Old password does not match");
                
                return;
            }

            // Verify that new password meets requirements
            Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
            Matcher matcher = pattern.matcher(newPassword.getText());
            if (!matcher.find()) {
                resultLabel.setText("Please ensure your password is at least 8 characters long, includes both letters and numbers and contains at least one uppercase letter and one special character");
                resultLabel.setWrapText(true);
                
                return;
            }

            // Verify that repeated password matches new password
            if (!repeatPassword.getText().equals(newPassword.getText())) {
                resultLabel.setText("Please ensure new passwords match");
                
                return;
            }

            PasswordChangeServiceImpl service = new PasswordChangeServiceImpl();
            passwordHash = HexFormat.of().formatHex(digest.digest(newPassword.getText().getBytes(StandardCharsets.UTF_8)));
            service.changePassword(this.user.getUsername(), passwordHash);
            resultLabel.setText("Password successfully changed!");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error in ChangePasswordController.onChangePasswordClick: " + e.getClass().getName());
            e.printStackTrace();
        }
    }
}
