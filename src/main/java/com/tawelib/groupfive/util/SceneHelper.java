package com.tawelib.groupfive.util;

import com.tawelib.groupfive.entity.Library;
import com.tawelib.groupfive.entity.User;
import com.tawelib.groupfive.fxmlcontroller.BaseFxmlController;
import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SceneHelper {

  private SceneHelper() {
    throw new IllegalStateException("Util class");
  }

  /**
   * Sets up a new scene. Saves important references in appropriate
   * controllers.
   *
   * @param primaryStage Primary stage reference.
   * @param library Library reference.
   * @param loggedInUser Logged-in user.
   * @param sceneName Scene name.
   * @throws IOException When unable to switch scenes.
   */
  public static void setUpScene(Stage primaryStage, Library library,
      User loggedInUser,
      String sceneName)
      throws IOException {
    URL resourceLocation = ResourceHelper.getViewUrl(sceneName);

    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(resourceLocation);
    loader.load();
    BaseFxmlController controller = loader.getController();

    Pane root = loader.getRoot();
    Scene scene = new Scene(root);

    primaryStage.setScene(scene);
    primaryStage.show();

    /*
    These two references need to be passed around the application so that all
    controllers can access the necessary data and operations.
     */
    if (controller != null) {
      controller.setLibrary(library);
      controller.setPrimaryStage(primaryStage);
      controller.setLoggedInUser(loggedInUser);
      controller.refresh();
    }
  }

  /**
   * Overloads for setUpScene(Stage primaryStage, Library library, String
   * sceneName).
   *
   * @param controller Controller that initiates the switch.
   * @param sceneName Scene name to switch to.
   */
  public static void setUpScene(BaseFxmlController controller,
      String sceneName) {
    try {
      setUpScene(
          controller.getPrimaryStage(),
          controller.getLibrary(),
          controller.getLoggedInUser(),
          sceneName
      );
    } catch (IOException e) {
      throw new Error("Could not set up scene.");
    }
  }
}
