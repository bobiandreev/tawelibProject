package com.tawelib.groupfive.entity;

import java.io.Serializable;

/**
 * Enum ResourceType Enum to differentiate between different types of resources.
 *
 * @author Shree Desai
 * @version 1.0
 */
public enum ResourceType implements Serializable {
  /**
   * Book resource type.
   */
  BOOK(28, 2, 25),
  /**
   * Dvd resource type.
   */
  DVD(14, 2, 25),
  /**
   * Laptop resource type.
   */
  LAPTOP(2, 10, 100),
  /**
   * Video game resource type.
   */
  GAME(7, 3, 35);


  private int loanDuration;
  private int fine;
  private int maxFine;

  /**
   * Enum Constructor for ResourceType.
   *
   * @param loanDuration Loan duration.
   * @param fine Fine amount.
   * @param maxFine Maximum fine.
   */
  private ResourceType(int loanDuration, int fine, int maxFine) {
    this.loanDuration = loanDuration;
    this.fine = fine;
    this.maxFine = maxFine;
  }

  /**
   * Gets loan duration.
   *
   * @return the loan duration
   */
  public int getLoanDuration() {
    return loanDuration;
  }

  /**
   * Gets fine.
   *
   * @return the fine
   */
  public int getFine() {
    return fine;
  }

  /**
   * Gets max fine.
   *
   * @return the max fine
   */
  public int getMaxFine() {
    return maxFine;
  }
}



