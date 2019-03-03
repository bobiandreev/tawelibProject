package com.tawelib.groupfive.fxmlcontroller;

import com.tawelib.groupfive.entity.ResourceType;
import com.tawelib.groupfive.util.SceneHelper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Pane;


/**
 * The Statistics controller: A user can view info about borrowing statistics for themselves and an
 * average user. They can also view the most popular resources over different time periods, with
 * specifics given for each resource type. A Librarian will also be able to statistics pertaining to
 * fines issued.
 *
 * @author Shree Desai
 * @version 1.0
 */
public class StatisticsController extends BaseFxmlController {

  @FXML
  private Button backButton;

  @FXML
  private Accordion statsContainer;

  @FXML
  private TitledPane userStatPane;

  @FXML
  private ComboBox<String> userStatTimeComboBox;

  @FXML
  private Label userLabel;

  @FXML
  private TextField noUserBorrowedTextField;

  @FXML
  private TextField noAverageBorrowedTextField;

  @FXML
  private StackedBarChart<?, ?> userStatBarChart;

  @FXML
  private CategoryAxis userStatXAxis;

  @FXML
  private NumberAxis userStatYAxis;

  @FXML
  private ComboBox<ResourceType> userStatResTypeComboBox;

  @FXML
  private TitledPane resourceStatPane;

  @FXML
  private ComboBox<ResourceType> resourceStatResTypeComboBox;

  @FXML
  private Pane popLaptopPane;

  @FXML
  private ComboBox<String> laptopStatTimeComboBox;

  @FXML
  private TableView<?> popularLaptopTableView;

  @FXML
  private PieChart popularOsPieChart;

  @FXML
  private Pane popDvdPane;

  @FXML
  private ComboBox<?> dvdStatTimeComboBox;

  @FXML
  private TableView<?> popularDvdTableView;

  @FXML
  private TableView<?> popDvdDirectorTableView1;

  @FXML
  private Pane popBookPane;

  @FXML
  private ComboBox<?> bookStatTimeComboBox;

  @FXML
  private TableView<?> popularBookTableView;

  @FXML
  private PieChart popBookGenrePieChart;

  @FXML
  private TableView<?> popBookAuthorTableView;

  @FXML
  private Pane popResourcePane;

  @FXML
  private TableView<?> popularResTableView;

  @FXML
  private ComboBox<?> resourceStatTimeComboBox;

  @FXML
  private Pane popVideoGamePane;

  @FXML
  private TableView<?> popularVideoGameTableView;

  @FXML
  private ComboBox<?> videoStatTimeComboBox;

  @FXML
  private TitledPane fineStatPane;

  @FXML
  private TextField aveFineAmountTextField;

  @FXML
  private ComboBox<?> fineStatTimeComboBox;

  @FXML
  private TextField totalFineAmountTextField;

  @FXML
  private ComboBox<?> fineStatResTypeComboBox;

  private ResourceType[] resourceTypes = {
      null,
      ResourceType.BOOK,
      ResourceType.DVD,
      ResourceType.LAPTOP,
      ResourceType.GAME

  };

  private ObservableList<String> timePeriods = FXCollections.observableArrayList(
      "Day", "Week", "Month");

  /**
   * Sets the dynamic fields.
   */
  @Override
  public void refresh() {
    setExpandedUserStatTitledPane();


  }

  /**
   * Returns to the user dashboard screen.
   */
  public void back() {
    SceneHelper.setUpScene(this, "UserDashboard");
  }

  /**
   * Sets nodes visible to only one type of users visible to the correct type of users only.
   */
  @Override
  protected void configureVisibilities() {
    librarianNodes = new Node[]{
        fineStatPane
    };


  }

  /**
   * Initializes nodes in the User Statistics Pane.
   */
  public void setExpandedUserStatTitledPane() {
    statsContainer.setExpandedPane(userStatPane);
    //Sets default value to first value in time period list.
    userStatTimeComboBox.setItems(timePeriods);
    userStatTimeComboBox.getSelectionModel().selectFirst();
    //Sets default value to first value in resource types list.
    userStatResTypeComboBox.getItems().addAll(resourceTypes);
    userStatResTypeComboBox.getSelectionModel().selectFirst();
    userStatTimeComboBoxHandler();
    userLabel.setVisible(isCustomerLoggedIn());
    noUserBorrowedTextField.setVisible(isCustomerLoggedIn());
  }

  public void userStatResTypeComboBoxHandler(){

  }

  public void userStatTimeComboBoxHandler(){

  }

  /**
   * Initializes nodes in the Resource Statistics Pane.
   */
  public void setExpandedResourceStatTitledPane() {
  }

  /**
   * Initializes nodes in the Fine Statistics Pane.
   */
  public void setExpandedFineStatTitledPane() {

  }


}
