package com.tawelib.groupfive.util;

import com.tawelib.groupfive.entity.Library;
import com.tawelib.groupfive.fxmlcontroller.BaseFxmlController;
import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Manages switching between scenes and passing important references around.
 *
 * @author Petr Hoffmann
 * @version 1.0
 */
public class SceneHelper {

  private SceneHelper() {
    throw new IllegalStateException("Util class");
  }

  /**
   * Sets up a new scene. Saves important references in appropriate controllers. Returns the new
   * controller.
   *
   * @param primaryStage Primary stage reference.
   * @param library Library reference.
   * @param nextSceneName The next Scene name.
   * @param lastSceneName The last scene name.
   * @return New scene controller.
   * @throws IOException When unable to switch scenes.
   */
  public static BaseFxmlController setUpScene(Stage primaryStage,
      Library library,
      String lastSceneName,
      String nextSceneName)
      throws IOException {
    URL resourceLocation = ResourceHelper.getViewUrl(nextSceneName);

    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(resourceLocation);
    loader.load();
    BaseFxmlController controller = loader.getController();

    controller.setLastSceneName(lastSceneName);
    controller.setCurrentSceneName(nextSceneName);

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
      controller.setVisibilitiesAndRefresh();
    }

    return controller;
  }

  /**
   * Overloads the setUpScene(Stage primaryStage, Library library, String sceneName).
   *
   * @param controller Controller that initiates the switch.
   * @param sceneName Scene name to switch to.
   * @return New scene controller.
   */
  public static BaseFxmlController setUpScene(BaseFxmlController controller,
      String sceneName) {
    try {
      return setUpScene(
          controller.getPrimaryStage(),
          controller.getLibrary(),
          controller.getCurrentSceneName(),
          sceneName

      );

    } catch (IOException e) {
      throw new Error("Could not set up scene.");
    }
  }

}
