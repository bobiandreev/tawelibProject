package com.tawelib.groupfive.manager;

import com.tawelib.groupfive.entity.Customer;
import com.tawelib.groupfive.entity.Lease;
import com.tawelib.groupfive.entity.Library;
import com.tawelib.groupfive.entity.ResourceType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * File name - Statistics manager. The Statistics Manager class controls data flow between the
 * Repositories and the Statistics GUI interface.
 *
 * @author Shree Desai
 * @version 1.0
 */
public class StatisticsManager {

  /**
   * Gets specific user statistics.
   *
   * @param library the library
   * @param customer the customer
   * @param resourceType the resource type
   * @param timePeriod the time period
   * @return the specific user statistics
   */
  public static int[] getSpecificUserStatistics(Library library, Customer customer,
      ResourceType resourceType, String timePeriod) {
    List<Lease> customerLeases;
    int[] result = new int[5];

    switch (resourceType) {
      case BOOK:
      case DVD:
      case LAPTOP:
      case GAME:
        customerLeases = library.getLeaseRepository()
            .getCustomerResourceTypeLeases(resourceType, customer);
        break;
      default:
        customerLeases = library.getLeaseRepository().getCustomerLeaseHistory(customer);
    }
    Collections.reverse(customerLeases);

    switch (timePeriod) {
      case "Day":
        result = getUserStatsDay(customerLeases);
        break;
      case "Week":
        //result = getUserStatsWeek(customerLeases);
        break;
      case "Month":
        result = getUserStatsMonth(customerLeases);
        break;
      default:
    }

    return result;

  }

  /**
   * Gets average user statistics.
   *
   * @param library the library
   * @param resourceType the resource type
   * @param timePeriod the time period
   * @return the average user statistics
   */
  public static int[] getAverageUserStatistics(Library library, ResourceType resourceType,
      String timePeriod) {

    List<Lease> leases;
    int[] result = new int[5];
    switch (resourceType) {
      case BOOK:
      case DVD:
      case LAPTOP:
      case GAME:
        leases = library.getLeaseRepository().getResourceTypeLeases(resourceType);
        break;
      default:
        leases = library.getLeaseRepository().getAll();
    }
    Collections.reverse(leases);

    switch (timePeriod) {
      case "Day":

        result = getUserStatsDay(leases);
        break;
      case "Week":
        //result = getUserStatsWeek(leases);
        break;
      case "Month":
        result = getUserStatsMonth(leases);
        break;
      default:
    }

    return result;
  }

  private static int[] getUserStatsDay(List<Lease> leases) {
    Map<LocalDateTime, Map<Customer, List<Lease>>> leasesMappedperDay = leases.stream()
        .filter(item -> item.getDateLeased().isAfter(LocalDateTime.now().minusDays(4)))
        .collect(Collectors.groupingBy(Lease::getDateLeased,
            Collectors.groupingBy(Lease::getBorrowingCustomer)));

    int[] totalByDate = new int[5];

    for (int count = 0; count < 5; count++) {
      int numberOfCustomers = leasesMappedperDay.get(LocalDateTime.now().minusDays(count)).size();
      int numberOfLeases = 0;
      for (int i = 0; i < numberOfCustomers; i++) {
        numberOfLeases =
            numberOfLeases + leasesMappedperDay.get(LocalDateTime.now().minusDays(count)).get(i)
                .size();
      }
      numberOfLeases = numberOfLeases / numberOfCustomers;
      totalByDate[count] = numberOfLeases;

    }

    return totalByDate;

  }

  /*
  private static int[] getUserStatsWeek(List<Lease> leases) {

  Map<LocalDateTime, Map<Customer, List<Lease>>> leasesMappedperWeek = leases.stream()
        .filter(item -> item.getDateLeased().isAfter(LocalDateTime.now().minusWeeks(4)))
        .collect( Collectors.groupingBy(Lease::getDateLeased,
            Collectors.groupingBy(Lease::getBorrowingCustomer)));

    return leases;
  }
  */

  private static int[] getUserStatsMonth(List<Lease> leases) {
    Map<LocalDateTime, Map<Customer, List<Lease>>> leasesMappedperMonth = leases.stream()
        .filter(item -> item.getDateLeased().isAfter(LocalDateTime.now().minusMonths(4)))
        .collect(Collectors.groupingBy(Lease::getDateLeased,
            Collectors.groupingBy(Lease::getBorrowingCustomer)));

    int[] totalByDate = new int[5];
    Map<Number, Map<Customer, List<Lease>>> leasesMappedByMonth;
    Iterator iterator = leasesMappedperMonth.entrySet().iterator();

    while (iterator.hasNext()) {

    }
    return totalByDate;
  }

}
