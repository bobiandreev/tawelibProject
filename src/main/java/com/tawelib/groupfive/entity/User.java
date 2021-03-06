package com.tawelib.groupfive.entity;

import com.tawelib.groupfive.runtime.SimulatedLocalDateTime;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * User.java User class stores personal information for the account holder (either a Customer or
 * Librarian).
 *
 * @author Shree Desai
 * @version 1.0
 */
public class User implements Serializable {

  protected String username;
  protected String firstName;
  protected String lastName;
  protected String phoneNumber;
  protected Address address;
  protected LocalDateTime lastLogin;

  /**
   * Creates an instance of the User class.
   *
   * @param firstName the first name
   * @param lastName the last name
   * @param phoneNumber the phone number
   * @param houseNumber the house number
   * @param street the street
   * @param city the city
   * @param postCode the post code
   */
  public User(String firstName, String lastName, String phoneNumber, String houseNumber,
      String street, String city, String postCode) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.phoneNumber = phoneNumber;
    this.address = new Address(houseNumber, street, city, postCode);
    //this.lastLogin = SimulatedLocalDateTime.now();
    this.lastLogin = LocalDateTime.now();
  }

  /**
   * Gets username.
   *
   * @return the username
   */
  public String getUsername() {
    return username;
  }

  /**
   * Gets first name.
   *
   * @return the first name
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * Sets first name.
   *
   * @param firstName the first name
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * Gets last name.
   *
   * @return the last name
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * Sets last name.
   *
   * @param lastName the last name
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * Returns full name.
   *
   * @return Full name.
   */
  public String getFullName() {
    return firstName + " " + lastName;
  }

  /**
   * Gets phone number.
   *
   * @return the phone number
   */
  public String getPhoneNumber() {
    return phoneNumber;
  }

  /**
   * Sets phone number.
   *
   * @param phoneNumber the phone number
   */
  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  /**
   * Gets address.
   *
   * @return the address
   */
  public Address getAddress() {
    return address;
  }

  /**
   * Sets address.
   *
   * @param address the address
   */
  public void setAddress(Address address) {
    this.address = address;
  }

  /**
   * Gets lastLogin.
   *
   * @return the lastLogin
   */
  public LocalDateTime getLastLogin() {
    return lastLogin;
  }

  /**
   * Sets lastLogin.
   *
   * @param lastLogin the the timestamp of the user's last login
   */
  public void setLastLogin(LocalDateTime lastLogin) {
    this.lastLogin = lastLogin;
  }
}
