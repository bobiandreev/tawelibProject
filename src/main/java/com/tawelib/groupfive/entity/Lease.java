package com.tawelib.groupfive.entity;

import com.tawelib.groupfive.repository.CopyRepository;

import java.io.Serializable;
import java.util.Date;

/**
 * Lease.java The ‘Lease’ class is the class that stores the information about
 * every loan, linking the relevant instance of the ‘Customer’ class and
 * instance of the ‘Copy’ class.
 *
 * @author Shree Desai
 * @version 0.2
 */
public class Lease implements Serializable {

  private Date dateLeased;
  private Date dateReturned;
  private Date dueDate;
  private final Copy copy;
  private final Customer borrowingCustomer;


  /**
   * Instantiates a new Lease.
   *
   * @param customer the borrowing customer username
   * @param copy the borrowed copy id
   */
  public Lease(Customer customer, Copy copy) {
    this.borrowingCustomer = customer;
    this.copy = copy;
    this.dateLeased = new Date();
  }

  /**
   * Gets date leased.
   *
   * @return the date leased
   */
  public Date getDateLeased() {
    return dateLeased;
  }

  /**
   * Gets date returned.
   *
   * @return the date returned
   */
  public Date getDateReturned() {
    return dateReturned;
  }

  /**
   * Sets date returned.
   */
  public void setDateReturned() {
    this.dateReturned = new Date();
  }

  /**
   * Gets due date.
   *
   * @return the due date
   */
  public Date getDueDate() {
    return dueDate;
  }

  /**
   * Sets due date.
   *
   * @param dueDate the due date
   */
  public void setDueDate(Date dueDate) {
    this.dueDate = dueDate;
  }


  /**
   * Returns the borrowed copy.
   *
   * @return the borrowed Copy.
   */
  public Copy getBorrowedCopy() {
    return copy;
  }


  /**
   * Gets borrowing customer.
   *
   * @return the borrowing customer
   */
  public Customer getBorrowingCustomer() {
    return borrowingCustomer;
  }


}
