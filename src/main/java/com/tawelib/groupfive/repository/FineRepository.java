package com.tawelib.groupfive.repository;

import com.tawelib.groupfive.entity.Customer;
import com.tawelib.groupfive.entity.Fine;
import com.tawelib.groupfive.entity.Lease;
import com.tawelib.groupfive.entity.ResourceType;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * File Name - FineRepository.java The Fine repository class handles instances of Fines.
 *
 * @author Shree Desai
 * @version 1.0
 */
public class FineRepository implements BaseRepository<Fine> {

  private ArrayList<Fine> fines;

  /**
   * Constructs a new FineRepository.
   */
  public FineRepository() {
    fines = new ArrayList<>();
  }

  /**
   * Gets customer fines.
   *
   * @param customer the customer
   * @return the customer fines
   */
  public List<Fine> getCustomerFines(Customer customer) {
    ArrayList<Fine> result = new ArrayList<>();

    for (Fine fine : fines) {
      if (fine.getLease().getBorrowingCustomer() == customer) {
        result.add(fine);
      }
    }

    return result;
  }

  /**
   * Gets resource type fines.
   *
   * @param resourceType the resource type
   * @return the resource type fines
   */
  public List<Fine> getResourceTypeFines(ResourceType resourceType) {
    ArrayList<Fine> result = new ArrayList<>();

    for (Fine fine : fines) {
      if (fine.getLease().getBorrowedCopy().getResource().getType() == resourceType) {
        result.add(fine);
      }
    }

    return result;
  }

  /**
   * Gets customer resource type fines.
   *
   * @param resourceType the resource type
   * @param customer the customer
   * @return the customer resource type fines
   */
  public List<Fine> getCustomerResourceTypeFines(ResourceType resourceType, Customer customer) {
    ArrayList<Fine> result = new ArrayList<>();

    for (Fine fine : getResourceTypeFines(resourceType)) {
      if (fine.getLease().getBorrowingCustomer() == customer) {
        result.add(fine);
      }
    }

    return result;
  }




  /**
   * {@inheritDoc}
   */
  @Override
  public List<Fine> getAll() {
    return fines;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void add(Fine fine) {
    if (!fines.contains(fine)) {
      fines.add(fine);
    }
  }
}
