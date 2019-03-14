package com.tawelib.groupfive.testdata;

import com.tawelib.groupfive.entity.Copy;
import com.tawelib.groupfive.entity.Customer;
import com.tawelib.groupfive.entity.Lease;
import com.tawelib.groupfive.entity.Library;
import com.tawelib.groupfive.exception.CopyUnavailableException;
import com.tawelib.groupfive.exception.OverResourceCapException;
import com.tawelib.groupfive.manager.CopyManager;
import com.tawelib.groupfive.runtime.SimulatedClock;
import com.tawelib.groupfive.runtime.SimulatedLocalDateTime;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Generates test and showcase data for user interactions.
 *
 * @author Petr Hoffmann
 * @version 1.1
 */
class OperationsTestData {

  private OperationsTestData() {
    throw new UnsupportedOperationException();
  }

  private static final double IDEAL_LEASES_CONCENTRATION = 0.2;
  private static final double LATE_RETURNS_CONCENTRATION = 0.075;

  private static final Random random = new Random();
  private static List<String> borrowedCopyIds = new ArrayList<>();
  private static List<String> borrowedCopyIdsForLateReturn = new ArrayList<>();

  private static int missesCounter = 0;
  private static int overCapCounter = 0;
  private static int borrowsCounter = 0;
  private static int returnsCounter = 0;
  private static int lateReturnsCounter = 0;

  /**
   * Generates operations test data. Simulates the users' behaviour by randomly deciding each next
   * action.
   *
   * @param library Library.
   */
  static void generate(Library library) {
    final int targetNumberOfLeases = (int) (library.getCopyRepository().getAll().size()
        * IDEAL_LEASES_CONCENTRATION);

    // While the simulated time hasn't caught up with the actual current time.
    while (SimulatedLocalDateTime.now().isBefore(LocalDateTime.now())) {

      if (!borrowedCopyIds.isEmpty()
          && random.nextInt(borrowedCopyIds.size() + borrowedCopyIdsForLateReturn.size())
          > targetNumberOfLeases / 2) {

        simulateLateReturn(library);
        simulateReturn(library);
      } else {
        simulateLoan(library);
      }

      SimulatedClock.addMinutes(random.nextInt(30));
    }

    System.out.println("Borrows: " + borrowsCounter);
    System.out.println("Returns: " + returnsCounter);
    System.out.println("Late Returns: " + lateReturnsCounter);
    System.out.println("Over Cap:" + overCapCounter);
    System.out.println("Misses: " + missesCounter);

    System.out.println("Simulation finished.");
  }

  /**
   * Simulates a loan for a random copy of a random resource by a random customer.
   *
   * @param library Library.
   */
  private static void simulateLoan(Library library) {
    String copyIdToBeBorrowed = getRandomCopyId(library);
    String randomCustomerUsername = getRandomCustomerUsername(library);

    try {
      CopyManager.borrowResourceCopy(
          library,
          copyIdToBeBorrowed,
          randomCustomerUsername
      );

      if (random.nextDouble() < LATE_RETURNS_CONCENTRATION) {
        borrowedCopyIdsForLateReturn.add(copyIdToBeBorrowed);
      } else {
        borrowedCopyIds.add(copyIdToBeBorrowed);
      }

      borrowsCounter++;
    } catch (CopyUnavailableException e) {
      missesCounter++;
    } catch (OverResourceCapException e) {
      overCapCounter++;
    }
  }

  /**
   * Simulates a return of a random borrowed copy.
   *
   * @param library Library.
   */
  private static void simulateReturn(Library library) {
    int randomIndex = random.nextInt(borrowedCopyIds.size());

    CopyManager.returnResourceCopy(
        library,
        borrowedCopyIds.get(randomIndex)
    );

    borrowedCopyIds.remove(randomIndex);

    returnsCounter++;
  }

  /**
   * Simulates a late return of one of the copies designated to be returned late.
   *
   * @param library Library.
   */
  private static void simulateLateReturn(Library library) {
    if (!borrowedCopyIdsForLateReturn.isEmpty()) {
      int randomIndex = random.nextInt(borrowedCopyIdsForLateReturn.size());

      String copyId = borrowedCopyIdsForLateReturn.get(randomIndex);

      Copy copy = library.getCopyRepository().getSpecific(copyId);
      Lease currentLease = library.getLeaseRepository().getCopyCurrentLease(copy);

      LocalDateTime dueDate = currentLease.getDueDate();

      if (dueDate != null && dueDate.isAfter(SimulatedLocalDateTime.now())) {
        CopyManager.returnResourceCopy(
            library,
            copyId
        );

        borrowedCopyIdsForLateReturn.remove(randomIndex);

        lateReturnsCounter++;
      }
    }
  }

  /**
   * Returns a random customer username.
   *
   * @param library Library.
   * @return Username.
   */
  private static String getRandomCustomerUsername(Library library) {
    List<Customer> customers = library.getCustomerRepository().getAll();

    int randomIndex = random.nextInt(customers.size());

    return customers.get(randomIndex).getUsername();
  }

  /**
   * Returns a random copy id of a random resource.
   *
   * @param library Library.
   * @return ID.
   */
  private static String getRandomCopyId(Library library) {
    List<Copy> copies = library.getCopyRepository().getAll();

    int randomIndex = random.nextInt(copies.size());

    return copies.get(randomIndex).getId();
  }
}
