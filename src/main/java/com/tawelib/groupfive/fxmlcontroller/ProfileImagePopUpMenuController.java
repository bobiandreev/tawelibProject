package com.tawelib.groupfive.fxmlcontroller;

import com.tawelib.groupfive.draw.Drawing;
import com.tawelib.groupfive.util.AlertHelper;
import com.tawelib.groupfive.util.FileSystemHelper;
import com.tawelib.groupfive.util.SceneHelper;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;

/**
 * The type Profile image pop up menu controller. This screen lets the user see all the default
 * images they can use for their profile. They are also given the option to select an image
 * from the computer if it is in the correct format or draw their own image.
 *
 * @author Oskars Dervinis, Petr Hoffman
 * @version 1.0
 */
public class ProfileImagePopUpMenuController extends BaseFxmlController {

  @FXML
  private ImageView imgOption1;

  @FXML
  private ImageView imgOption2;

  @FXML
  private ImageView imgOption3;

  @FXML
  private ImageView imgOption4;

  @FXML
  private ImageView imgOption5;

  @FXML
  private ImageView imgOption6;

  /**
   * Instantiates a new Profile image pop up menu controller.
   */
  public ProfileImagePopUpMenuController() {
  }

  /**
   * Sets the dynamic fields.
   */
  @Override
  public void refresh() {
    //sets the predefined images
    ImageView[] standartImg = new ImageView[6];
    standartImg[0] = imgOption1;
    standartImg[1] = imgOption2;
    standartImg[2] = imgOption3;
    standartImg[3] = imgOption4;
    standartImg[4] = imgOption5;
    standartImg[5] = imgOption6;
    for (int i = 0; i < standartImg.length; i++) {
      Image img = new Image(
          "file:" + "data/images/profile/default/default_" + i + ".png");
      if (img != null) {
        standartImg[i].setImage(img);
        standartImg[i].setOnMouseClicked((evt) -> {
          try {
            //allows user to choose image file from their computer
            File file = new File(
                FileSystemHelper.getUserProfilePicturePath(loggedInUser));
            BufferedImage bufferedImage = SwingFXUtils
                .fromFXImage(((ImageView) evt.getSource()).getImage(), null);
            ImageIO.write(bufferedImage, "png", file);

            AlertHelper.alert(AlertType.INFORMATION,
                "Profile image set successfully.");
            back();
          } catch (IOException e) {
            AlertHelper.alert(AlertType.ERROR, "Error setting image.");
          }
        });
      }
    }
  }

  /**
   * Returns to the user dashboard.
   */
  public void back() {
    SceneHelper.setUpScene(this, "UserDashboard");
  }

  /**
   * Launches an app for drawing custom images.
   */
  public void drawYourOwn() {
    Platform.runLater(new Runnable() {
      public void run() {
        Drawing drawingApp = new Drawing();
        drawingApp.startWithUserReference(loggedInUser);
      }
    });
  }

  /**
   * The method creates the window with selecting the image to set as a profile
   * Called from pressing a button chooseFileImg, accepts only png format.
   */
  public void choosePicture() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.getExtensionFilters()
        .add(new FileChooser.ExtensionFilter("PNG", "*.png"));
    File file = fileChooser.showOpenDialog(getPrimaryStage());
    if (file != null) {
      try {
        File currProfImg = new File(
            FileSystemHelper.getUserProfilePicturePath(loggedInUser));
        BufferedImage tempImg = ImageIO.read(file);
        ImageIO.write(tempImg, "png", currProfImg);
      } catch (IOException e) {
        AlertHelper.alert(AlertType.ERROR, "Unable to load image.");
      }

      AlertHelper
          .alert(AlertType.INFORMATION, "Profile image set successfully.");

      back();
    }
  }
}
