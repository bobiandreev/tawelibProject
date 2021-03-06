package com.tawelib.groupfive.repository;

import com.tawelib.groupfive.entity.Copy;
import com.tawelib.groupfive.entity.CopyStatus;
import com.tawelib.groupfive.entity.Customer;
import com.tawelib.groupfive.entity.Resource;
import com.tawelib.groupfive.exception.CopyUnavailableException;
import com.tawelib.groupfive.exception.EntityNotFoundException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * File Name - CopyRepository.java The Copy repository class handles copy details.
 *
 * @author Created by Themis, Modified by Shree Desai
 * @version 1.0
 */
public class CopyRepository implements BaseRepository<Copy> {

  private ArrayList<Copy> copies;

  private long lastCopyId = 0;

  public CopyRepository() {
    copies = new ArrayList<>();
  }

  /**
   * Gets a specific copy.
   *
   * @param copyId the copy id
   * @return the specific
   */
  public Copy getSpecific(String copyId) {
    for (Copy copy : copies) {
      if (copy.getId().equals(copyId)) {
        return copy;
      }
    }
    return null;
  }

  /**
   * Gets borrowed copies.
   *
   * @param customer The Customer.
   * @return the borrowed copies.
   */
  public List<Copy> getBorrowedCopies(Customer customer) {
    ArrayList<Copy> result = new ArrayList<>();

    for (Copy copy : copies) {
      if (copy.getBorrowingCustomer() == customer) {
        result.add(copy);
      }
    }

    return result;
  }

  /**
   * Gets specific reserved.
   *
   * @param customer the customer
   * @param resource the resource
   * @return the specific reserved
   * @throws EntityNotFoundException When no reserved copy found.
   */
  public Copy getSpecificReserved(Customer customer, Resource resource)
      throws EntityNotFoundException {
    for (Copy copy : getResourceCopies(resource)) {
      if (copy.getBorrowingCustomer() == customer && copy.getStatus()
          .equals(CopyStatus.RESERVED)) {
        return copy;
      }
    }

    throw new EntityNotFoundException();
  }

  /**
   * Gets resource copies.
   *
   * @param resource Resource.
   * @return the resource copies
   */
  public List<Copy> getResourceCopies(Resource resource) {
    ArrayList<Copy> result = new ArrayList<>();

    for (Copy copy : copies) {
      if (copy.getResource() == resource) {
        result.add(copy);
      }
    }
    return result;
  }

  /**
   * Get available resource copies list.
   *
   * @param resource the resource
   * @return the list
   */
  public List<Copy> getAvailableResourceCopies(Resource resource) {
    ArrayList<Copy> result = new ArrayList<>();

    for (Copy copy : getResourceCopies(resource)) {
      if (copy.getStatus() == CopyStatus.AVAILABLE) {
        result.add(copy);
      }
    }
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Copy> getAll() {
    return copies;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void add(Copy copy) {
    if (!copies.contains((copy))) {
      generateId(copy);
      copies.add(copy);
    }
  }

  /**
   * Generates a unique id for copies.
   *
   * @param copy Copy.
   */
  private void generateId(Copy copy) {
    String generatedCopyId = String.format("C%d", lastCopyId);

    try {
      Field idField = copy.getClass().getDeclaredField("id");
      idField.setAccessible(true);
      idField.set(copy, generatedCopyId);
      idField.setAccessible(false);
    } catch (IllegalAccessException | NoSuchFieldException e) {
      e.printStackTrace();
    } finally {
      lastCopyId++;
    }
  }

  public long getLastCopyId() {
    return lastCopyId;
  }
}
