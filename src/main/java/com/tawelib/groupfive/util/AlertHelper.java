package com.tawelib.groupfive.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Simplifies showing Alerts inside of a JavaFX application.
 *
 * @author Petr Hoffmann
 * @version 1.0
 */
public class AlertHelper {

  private AlertHelper() {
    throw new IllegalStateException("Util class.");
  }

  /**
   * Shows an alert message.
   *
   * @param alertType Alert type.
   * @param message Message.
   */
  public static void alert(AlertType alertType, String message) {
    Alert alert = new Alert(alertType);
    alert.setHeaderText(alertType.toString());
    alert.setContentText(message);
    alert.showAndWait();
  }

  /**
   * Event description.
   *
   * @param alertType the alert type
   * @param message the message
   */
  public static void eventDescription(AlertType alertType, String message) {
    Alert alert = new Alert(alertType,message);
    alert.setResizable(true);
    alert.setHeaderText("Information about the event");
    alert.showAndWait();
  }
}
