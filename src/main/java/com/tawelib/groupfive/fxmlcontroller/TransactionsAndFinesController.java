package com.tawelib.groupfive.fxmlcontroller;

import com.tawelib.groupfive.util.SceneHelper;

public class TransactionsAndFinesController extends BaseFxmlController {


  public TransactionsAndFinesController() {
  }

  /**
   * Sets the dynamic fields.
   */
  @Override
  public void refresh() {
    //TODO: Populate the table, etc.
  }

  public void back() {
    SceneHelper.setUpScene(this, "UserDashboard");
  }
}
