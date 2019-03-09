package com.tawelib.groupfive.fxmlcontroller;

import static javafx.scene.control.Alert.AlertType.CONFIRMATION;
import static javafx.scene.control.Alert.AlertType.INFORMATION;

import com.tawelib.groupfive.entity.Event;
import com.tawelib.groupfive.entity.Library;
import com.tawelib.groupfive.manager.EventManager;
import com.tawelib.groupfive.util.AlertHelper;
import com.tawelib.groupfive.util.SceneHelper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

/**
 * The class controls Events.fxml file, allows to observe event, see the users past events, see next
 * events and joined events. Also for librarian is available creation of the new event.
 *
 * @author Oskars Dervinis
 * @version 1.0
 */
public class EventsController extends BaseFxmlController {

  private static final double EVENT_CELL_WIDTH = 380.0;
  private static final double EVENT_CELL_HEIGHT = 100.0;
  private static final double JOIN_BUTTON_HEIGHT = 10.0;
  private static final String LEAVING_EVENT_CONFIRMATION
      = "Are you sure you want to leave the event '";
  private static final String LEAVING_EVENT_SUCCESS = "Successfully left the event!";
  private static final String JOINING_EVENT_CONFIRMATION
      = "Are you sure you want to join the event '";
  private static final String JOINING_EVENT_SUCCESS = "Successfully joined the event!";

  @FXML
  private Button newEventBtn;
  @FXML
  private ListView<VBox> upcomingEventsField;

  @FXML
  private ListView<VBox> currentEventsField;

  /**
   * initializes the GUI, adds 2 list views of events.
   */
  @FXML
  public void initialize() {
    if (isCustomerLoggedIn()) {
      newEventBtn.setVisible(false);//Creation event is not allowed
    }
  }

  @Override
  public void refresh() {
    upcomingEventsField.getItems().remove(0, upcomingEventsField.getItems().size());
    currentEventsField.getItems().remove(0, currentEventsField.getItems().size());
    initUpcomingEvents();
    initJoinedEvents();
  }

  /**
   * The method adds upcoming events to the scrolling pane.
   */
  private void initUpcomingEvents() {
    ArrayList<Event> allEvents = library.getEventRepository().getUpcomingEvents();
    for (int i = 0; i < allEvents.size(); i++) {
      upcomingEventsField.getItems().addAll(constructEventCell(allEvents.get(i), false));
    }
  }

  /**
   * The method adds already joined events to the scrolling pane.
   */
  private void initJoinedEvents() {
    ArrayList<Event> allEvents = EventManager.getCurrentParticipations(library, loggedInUser);

    for (int i = 0; i < allEvents.size(); i++) {
      currentEventsField.getItems().addAll(constructEventCell(allEvents.get(i), true));
    }
  }

  /**
   * The method opens the scene to view past events where this user participated. Called by pressing
   * pastEventsBtn.
   */
  public void showPastEvents() {
    SceneHelper.setUpScene(this, "PastEvents");
  }

  /**
   * The method opens the scene to create the new event. Is visible only for librarian. Called by
   * pressint createBtn button.
   */
  public void createNewEvent() {
    SceneHelper.setUpScene(this, "CreateEvent");
  }

  /**
   * The method creates the "cell" consisting of 2 buttons(for more info about the event and the
   * button for signing for event. It is reppresentated as VBox. Also checks if the user can join
   * the event or not.
   *
   * @param e the event will be displayed.
   * @param isRegistered a boolean which suggest whatever the user has registered for event
   * @return the VBox as representation of the event.
   */
  private VBox constructEventCell(Event e, boolean isRegistered) {
    VBox box = new VBox();

    Button showDescrBtn = new Button(e.getEventDate().toString().substring(0, 10) + "\n"
        + e.getEventDate().toString().substring(11, 16) + "\n" + e.getEventName() + "\n");
    showDescrBtn.setPrefSize(EVENT_CELL_WIDTH, EVENT_CELL_HEIGHT);
    Button signUpForEvent = new Button("Join");
    signUpForEvent.setPrefSize(EVENT_CELL_WIDTH, JOIN_BUTTON_HEIGHT);
    box.getChildren().addAll(showDescrBtn, signUpForEvent);
    eventButtonActions(e, showDescrBtn, signUpForEvent, isRegistered);
    if (isRegistered) {
      signUpForEvent.setText("Leave");
    }
    /*Checks if there are free slots and person dont participate, if not - disables the button*/

    if (!isRegistered && ((EventManager.eventFull(library, e))
        || (library.getParticipationRepository().doesParticipate(e, loggedInUser)))) {
      signUpForEvent.setDisable(true);
    }

    return box;
  }

  /**
   * The method states the actions taken after pressing the buttons which are part of VBox event in
   * the scrolling pane.
   *
   * @param e the event this button belongs
   * @param description A button which opens the full info about the event
   * @param isCurrent a boolean which indicates whatever the event has been joined by user
   * @param join The button for joining the event
   */
  private void eventButtonActions(Event e, Button description, Button join, boolean isCurrent) {

    description.setOnAction(event -> {
      String aboutEvent =
          e.getEventDate().toString().substring(0, 10) + "\n"
              + e.getEventDate().toString().substring(11, 16) + "\n" + e.getEventName()
              + "\n" + e.getDescription() + "\nFree slots: "
              + (e.getCapacity() - library.getParticipationRepository().getNumberOfParticipants(e));
      AlertHelper.eventDescription(INFORMATION, aboutEvent);
    });

    join.setOnAction(event -> {
      String alertConfirm;
      if (isCurrent) {
        alertConfirm = LEAVING_EVENT_CONFIRMATION;
      } else {
        alertConfirm = JOINING_EVENT_CONFIRMATION;
      }
      Alert alert = new Alert(CONFIRMATION, alertConfirm + e.getEventName() + "'?");
      Optional<ButtonType> answer = alert.showAndWait();
      if (answer.get() == ButtonType.OK) {
        if (isCurrent) {
          library.getParticipationRepository().removeParticipation(e, loggedInUser);
          AlertHelper.alert(INFORMATION, LEAVING_EVENT_SUCCESS);
        } else {
          EventManager.joinEvent(library, loggedInUser, e);
          AlertHelper.alert(INFORMATION, JOINING_EVENT_SUCCESS);
        }

        refresh();
      }
    });
  }

  /**
   * Returns to the previous screen.
   */
  @Override
  public void back() {
    SceneHelper.setUpScene(this, "UserDashboard");
  }

}