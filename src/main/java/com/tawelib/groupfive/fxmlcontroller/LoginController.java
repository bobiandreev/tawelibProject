package com.tawelib.groupfive.fxmlcontroller;

import com.tawelib.groupfive.entity.User;
import com.tawelib.groupfive.exception.AuthenticationException;
import com.tawelib.groupfive.manager.UserManager;
import com.tawelib.groupfive.repository.UserRepository;
import com.tawelib.groupfive.util.SceneHelper;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Controls the login screen where a user can choose to login as a librarian or not and access
 * their account if the information they have entered is a valid user.
 *
 * @author Petr Hoffmann
 * @version 1.0
 */
public class LoginController extends BaseFxmlController {

  @FXML
  private TextField usernameTextField;

  @FXML
  private Label usernameLabel;

  @FXML
  private CheckBox librarianCheckBox;

  public LoginController() {
  }

  /**
   * Changes the login info label depending on who is trying to log in.
   */
  public void toggleLibrarian() {
    if (librarianCheckBox.isSelected()) {
      usernameLabel.setText("Username or staff ID");
    } else {
      usernameLabel.setText("Username");
    }
  }

  /**
   * Authenticates a User (Customer or Librarian) in or shows an error dialog.
   */
  public void signIn() {
    UserRepository<? extends User> userRepository;

    if (librarianCheckBox.isSelected()) {
      userRepository = library.getLibrarianRepository();
    } else {
      userRepository = library.getCustomerRepository();
    }

    try {
      loggedInUser = userRepository.authenticate(usernameTextField.getText());
      lastLogin = loggedInUser.getLastLogin();
      UserManager.updateUserlastLogin(loggedInUser);

      SceneHelper.setUpScene(this, "UserDashboard");
    } catch (AuthenticationException e) {
      Alert alert = new Alert(AlertType.WARNING);
      alert.setHeaderText("User not found.");
      alert.setContentText("Try different credentials.");
      alert.showAndWait();
    }
  }
}
