package com.tawelib.groupfive.manager;

import static java.util.Comparator.comparing;

import com.tawelib.groupfive.entity.Book;
import com.tawelib.groupfive.entity.Customer;
import com.tawelib.groupfive.entity.Dvd;
import com.tawelib.groupfive.entity.Fine;
import com.tawelib.groupfive.entity.Game;
import com.tawelib.groupfive.entity.Laptop;
import com.tawelib.groupfive.entity.Lease;
import com.tawelib.groupfive.entity.Library;
import com.tawelib.groupfive.entity.Resource;
import com.tawelib.groupfive.entity.ResourceType;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * File name - Statistics manager. The Statistics Manager class controls data flow between the
 * Repositories and the Statistics GUI interface.
 *
 * @author Shree Desai
 * @version 1.0
 */
public class StatisticsManager {

  private static final int NUMBER_OF_POPULAR_RESOURCES = 10;

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

    //Gets a different set of leases based upon resource type
    if (resourceType == null) {
      customerLeases = library.getLeaseRepository().getCustomerLeaseHistory(customer);
    } else {
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
    }
    Collections.reverse(customerLeases);

    //Calls different methods based upon Time Period
    switch (timePeriod) {
      case "Day":
        result = getSpecificUserStatsDay(customerLeases);
        break;
      case "Week":
        result = getSpecificUserStatsWeek(customerLeases);
        break;
      case "Month":
        result = getSpecificUserStatsMonth(customerLeases);
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
  public static double[] getAverageUserStatistics(Library library, ResourceType resourceType,
      String timePeriod) {

    List<Lease> leases;
    double[] result = new double[5];

    //Gets a different set of Leases based upon resource type
    if (resourceType == null) {
      leases = library.getLeaseRepository().getAll();
    } else {
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
    }
    Collections.reverse(leases);
    //Total number of customer
    int customerSize = library.getCustomerRepository().getAll().size();

    //Calls different methods to analyze data based upon time periods
    switch (timePeriod) {
      case "Day":
        result = getAverageUserStatsDay(leases, customerSize);
        break;
      case "Week":
        result = getAverageUserStatsWeek(leases, customerSize);
        break;
      case "Month":
        result = getAverageUserStatsMonth(leases, customerSize);
        break;
      default:
    }

    return result;
  }

  /**
   * Gets fine statistics.
   *
   * @param library the library
   * @param resourceType the resource type
   * @param timePeriod the time period
   * @return the average fine statistics
   */
  public static double[][] getFineStatistics(Library library, ResourceType resourceType,
      String timePeriod) {

    List<Fine> fines;
    double[][] result = new double[2][5];

    //Gets a different set of Fines based upon Resource type
    if (resourceType == null) {
      fines = library.getFineRepository().getAll();
    } else {
      switch (resourceType) {
        case BOOK:
        case DVD:
        case LAPTOP:
        case GAME:
          fines = library.getFineRepository().getResourceTypeFines(resourceType);
          break;
        default:
          fines = library.getFineRepository().getAll();
      }
    }
    Collections.reverse(fines);

    //Calls different methods based upon time period
    switch (timePeriod) {
      case "Day":
        result = getFineStatsDay(fines);
        break;
      case "Week":
        result = getFineStatsWeek(fines);
        break;
      case "Month":
        result = getFineStatsMonth(fines);
        break;
      default:
    }

    return result;
  }

  /**
   * Works out the top most popular resources loaned within specified time period.
   *
   * @param library library
   * @param timePeriod "Day", "Week", "Month"
   * @param resourceType the type of resource you want to find out
   * @return a list of most popular resources
   */
  public static List<Resource> getPopularResources(Library library, String timePeriod,
      ResourceType resourceType) {
    //Gets all leases pertaining to a certain resource type.
    List<Lease> leases = library.getLeaseRepository().getResourceTypeLeases(resourceType);

    // Gets list of leases of same type that were borrowed within given time period
    switch (timePeriod) {
      case "Day":
        leases = leases.stream()
            .filter(item -> item.getDateLeased().isAfter(LocalDateTime.now().minusDays(1)))
            .collect(Collectors.toList());
        break;
      case "Week":
        leases = leases.stream()
            .filter(item -> item.getDateLeased().isAfter(LocalDateTime.now().minusDays(7)))
            .collect(Collectors.toList());
        break;
      case "All Time":
        break;
      default:
    }

    //Sorts leases in order of Date Leased.
    leases.sort(comparing(Lease::getDateLeased));
    HashMap<Resource, Integer> map = new HashMap<>();

    //Fills HashMap with every Resource loaned in time period and No. of times leased.
    for (Lease lease : leases) {
      //Makes the Resource into a Key to use for HashMap
      Resource key = lease.getBorrowedCopy().getResource();
      //Checks if resource has been previously inserted into the map
      if (map.containsKey(key)) {
        //Increments counter for Number of Lease of that Resource
        map.replace(key, (map.get(key)) + 1);
      } else {
        //Adds Resource to Map if not already added
        map.put(key, 1);
      }
    }

    //Changes the HashMap to an ArrayList
    Object[] keys = map.keySet().toArray();
    ArrayList<Integer> freq = new ArrayList<>();
    for (int i = 0; i < keys.length; i++) {
      freq.add(map.get((Resource) keys[i]));
    }

    //Sorts Arraylist in descending order
    Collections.sort(freq);
    Collections.reverse(freq);

    int loop;
    if (freq.size() > NUMBER_OF_POPULAR_RESOURCES) {
      loop = NUMBER_OF_POPULAR_RESOURCES;
    } else {
      loop = freq.size();
    }

    ArrayList<Resource> popularResources = new ArrayList<>();
    //Gets the 10 most popular resources
    for (int i = 0; i < loop; i++) {
      for (Object key : keys) {
        Resource resource = (Resource) key;
        //Checks if it is already in PopularResources
        if ((map.get(resource).equals(freq.get(i))) && (!popularResources
            .contains(resource)) && (
            popularResources.size() < NUMBER_OF_POPULAR_RESOURCES)) {
          popularResources.add(resource);
        }
      }
    }
    return popularResources;
  }

  /**
   * Works out the top most popular authors loaned within specified time period.
   *
   * @param library library
   * @param timePeriod "Day", "Week", "Month"
   * @return a list of most popular authors
   */
  public static List<String> getPopularAuthors(Library library, String timePeriod) {
    List<Lease> leases = library.getLeaseRepository().getResourceTypeLeases(ResourceType.BOOK);

    // Gets list of leases of same type that were borrowed within given time period
    switch (timePeriod) {
      case "Day":
        leases = leases.stream()
            .filter(item -> item.getDateLeased().isAfter(LocalDateTime.now().minusDays(1)))
            .collect(Collectors.toList());
        break;
      case "Week":
        leases = leases.stream()
            .filter(item -> item.getDateLeased().isAfter(LocalDateTime.now().minusDays(7)))
            .collect(Collectors.toList());
        break;
      case "All Time":
        break;
      default:
    }

    //Sorts leases in order of Date Leased.
    leases.sort(comparing(Lease::getDateLeased));
    HashMap<String, Integer> map = new HashMap<>();

    //Fills HashMap with every Author of every Book lease in time period and No. of times leased.
    for (Lease lease : leases) {
      //Makes the Resource into a Key to use for HashMap
      Book book = (Book) lease.getBorrowedCopy().getResource();
      String key = book.getAuthor();
      //Checks if resource has been previously inserted into the map
      if (map.containsKey(key)) {
        //Increments counter for Number of Lease of that Resource
        map.replace(key, (map.get(key)) + 1);
      } else {
        //Adds Resource to Map if not already added
        map.put(key, 1);
      }
    }

    //Changes the HashMap to an ArrayList
    Object[] keys = map.keySet().toArray();
    ArrayList<Integer> freq = new ArrayList<>();
    for (int i = 0; i < keys.length; i++) {
      freq.add(map.get((String) keys[i]));
    }

    //Sorts Arraylist in descending order
    Collections.sort(freq);
    Collections.reverse(freq);

    ArrayList<String> popularAuthors = new ArrayList<>();
    //Gets the 5 most popular authors
    for (int i = 0; i < 10; i++) {
      try {
        for (Object key : keys) {
          String author = (String) key;
          //Checks if it is already in PopularResources
          if ((map.get(author).equals(freq.get(i))) && (!popularAuthors.contains(author)) && (
              popularAuthors.size() < 10)) {
            popularAuthors.add(author);
          }
        }
      } catch (IndexOutOfBoundsException e) {
        continue;
      }
    }
    return popularAuthors;
  }

  /**
   * Works out the top most popular directors loaned within specified time period.
   *
   * @param library library
   * @param timePeriod "Day", "Week", "Month"
   * @return a list of most popular directors
   */
  public static List<String> getPopularDirectors(Library library, String timePeriod) {
    List<Lease> leases = library.getLeaseRepository().getResourceTypeLeases(ResourceType.DVD);

    // Gets list of leases of same type that were borrowed within given time period
    switch (timePeriod) {
      case "Day":
        leases = leases.stream()
            .filter(item -> item.getDateLeased().isAfter(LocalDateTime.now().minusDays(1)))
            .collect(Collectors.toList());
        break;
      case "Week":
        leases = leases.stream()
            .filter(item -> item.getDateLeased().isAfter(LocalDateTime.now().minusDays(7)))
            .collect(Collectors.toList());
        break;
      case "All Time":
        break;
      default:
    }

    //Sorts leases in order of Date Leased.
    leases.sort(comparing(Lease::getDateLeased));
    HashMap<String, Integer> map = new HashMap<>();

    //Fills HashMap with every Author of every Book lease in time period and No. of times leased.
    for (Lease lease : leases) {
      //Makes the Resource into a Key to use for HashMap
      Dvd dvd = (Dvd) lease.getBorrowedCopy().getResource();
      String key = dvd.getDirector();
      //Checks if resource has been previously inserted into the map
      if (map.containsKey(key)) {
        //Increments counter for Number of Lease of that Resource
        map.replace(key, (map.get(key)) + 1);
      } else {
        //Adds Resource to Map if not already added
        map.put(key, 1);
      }
    }

    //Changes the HashMap to an ArrayList
    Object[] keys = map.keySet().toArray();
    ArrayList<Integer> freq = new ArrayList<>();
    for (int i = 0; i < keys.length; i++) {
      freq.add(map.get((String) keys[i]));
    }

    //Sorts Arraylist in descending order
    Collections.sort(freq);
    Collections.reverse(freq);

    ArrayList<String> popularDirectors = new ArrayList<>();
    //Gets the 5 most popular resources
    for (int i = 0; i < 10; i++) {
      try {
        for (Object key : keys) {
          String director = (String) key;
          //Checks if it is already in PopularDirectors
          if ((map.get(director).equals(freq.get(i))) && (!popularDirectors.contains(director))
              && (
              popularDirectors.size() < 10)) {
            popularDirectors.add(director);
          }
        }
      } catch (IndexOutOfBoundsException e) {
        continue;
      }
    }
    return popularDirectors;
  }

  /**
   * Works out the top most popular book genres and their frequency loaned within specified time
   * period.
   *
   * @param library library
   * @param timePeriod "Day", "Week", "Month"
   * @return a hashmap with each genre and frequency among leases of time period
   */
  public static HashMap<String, Integer> getPopularBookGenre(Library library, String timePeriod) {
    //Get all leases pertaining to books
    List<Lease> leases = library.getLeaseRepository().getResourceTypeLeases(ResourceType.BOOK);

    // Gets list of leases of same type that were borrowed within given time period
    switch (timePeriod) {
      case "Day":
        leases = leases.stream()
            .filter(item -> item.getDateLeased().isAfter(LocalDateTime.now().minusDays(1)))
            .collect(Collectors.toList());
        break;
      case "Week":
        leases = leases.stream()
            .filter(item -> item.getDateLeased().isAfter(LocalDateTime.now().minusDays(7)))
            .collect(Collectors.toList());
        break;
      case "Month":
        leases = leases.stream()
            .filter(item -> item.getDateLeased().isAfter(LocalDateTime.now().minusMonths(1)))
            .collect(Collectors.toList());
        break;
      default:
    }

    //Sorts leases in order of Date Leased.
    leases.sort(comparing(Lease::getDateLeased));
    HashMap<String, Integer> freqMap = new HashMap<>();

    //Fills HashMap with every Author of every Book lease in time period and No. of times leased.
    for (Lease lease : leases) {
      //Makes the Resource into a Key to use for HashMap
      Book book = (Book) lease.getBorrowedCopy().getResource();
      String key = book.getGenre();
      //Checks if resource has been previously inserted into the map
      if (freqMap.containsKey(key)) {
        //Increments counter for Number of Lease of that Resource
        freqMap.replace(key, (freqMap.get(key)) + 1);
      } else {
        //Adds Resource to Map if not already added
        freqMap.put(key, 1);
      }
    }

    return freqMap;
  }

  /**
   * Works out the top most popular laptop OS and their frequency loaned within specified time
   * period.
   *
   * @param library library
   * @param timePeriod "Day", "Week", "Month"
   * @return a hashmap with each OS and frequency among leases of time period
   */
  public static HashMap<String, Integer> getPopularLaptopOs(Library library, String timePeriod) {
    List<Lease> leases = library.getLeaseRepository().getResourceTypeLeases(ResourceType.LAPTOP);

    // Gets list of leases of same type that were borrowed within given time period
    switch (timePeriod) {
      case "Day":
        leases = leases.stream()
            .filter(item -> item.getDateLeased().isAfter(LocalDateTime.now().minusDays(1)))
            .collect(Collectors.toList());
        break;
      case "Week":
        leases = leases.stream()
            .filter(item -> item.getDateLeased().isAfter(LocalDateTime.now().minusDays(7)))
            .collect(Collectors.toList());
        break;
      case "Month":
        leases = leases.stream()
            .filter(item -> item.getDateLeased().isAfter(LocalDateTime.now().minusMonths(1)))
            .collect(Collectors.toList());
        break;
      default:
    }

    //Sorts leases in order of Date Leased.
    leases.sort(comparing(Lease::getDateLeased));
    HashMap<String, Integer> freqMap = new HashMap<>();

    //Fills HashMap with every Author of every Book lease in time period and No. of times leased.
    for (Lease lease : leases) {
      //Makes the Resource into a Key to use for HashMap
      Laptop laptop = (Laptop) lease.getBorrowedCopy().getResource();
      String key = laptop.getInstalledOperatingSystem();
      //Checks if resource has been previously inserted into the map
      if (freqMap.containsKey(key)) {
        //Increments counter for Number of Lease of that Resource
        freqMap.replace(key, (freqMap.get(key)) + 1);
      } else {
        //Adds Resource to Map if not already added
        freqMap.put(key, 1);
      }
    }

    return freqMap;
  }

  /**
   * Works out the top most popular videogame genres and their frequency loaned within specified
   * time period.
   *
   * @param library library
   * @param timePeriod "Day", "Week", "Month"
   * @return a hashmap with each genre and frequency among leases of time period
   */
  public static HashMap<String, Integer> getPopularVideogameGenre(Library library,
      String timePeriod) {
    List<Lease> leases = library.getLeaseRepository().getResourceTypeLeases(ResourceType.GAME);

    // Gets list of leases of same type that were borrowed within given time period
    switch (timePeriod) {
      case "Day":
        leases = leases.stream()
            .filter(item -> item.getDateLeased().isAfter(LocalDateTime.now().minusDays(1)))
            .collect(Collectors.toList());
        break;
      case "Week":
        leases = leases.stream()
            .filter(item -> item.getDateLeased().isAfter(LocalDateTime.now().minusDays(7)))
            .collect(Collectors.toList());
        break;
      case "Month":
        leases = leases.stream()
            .filter(item -> item.getDateLeased().isAfter(LocalDateTime.now().minusMonths(1)))
            .collect(Collectors.toList());
        break;
      default:
    }

    //Sorts leases in order of Date Leased.
    leases.sort(comparing(Lease::getDateLeased));
    HashMap<String, Integer> freqMap = new HashMap<>();

    //Fills HashMap with every Author of every Book lease in time period and No. of times leased.
    for (Lease lease : leases) {
      //Makes the Resource into a Key to use for HashMap
      Game game = (Game) lease.getBorrowedCopy().getResource();
      String key = game.getGenre();
      //Checks if resource has been previously inserted into the map
      if (freqMap.containsKey(key)) {
        //Increments counter for Number of Lease of that Resource
        freqMap.replace(key, (freqMap.get(key)) + 1);
      } else {
        //Adds Resource to Map if not already added
        freqMap.put(key, 1);
      }
    }

    return freqMap;
  }

  /**
   * Works out amount of leases per day for last 5 days.
   *
   * @param leases Records of what users borrowed
   * @return Array of amount of leases for user for last 5 days
   */
  private static int[] getSpecificUserStatsDay(List<Lease> leases) {

    //Groups all Leases by Date Leased.
    //It then filters out any leases that was before 4 days ago.
    Map<LocalDateTime, List<Lease>> leasesMappedPerDay = leases.stream()
        .filter(item -> item.getDateLeased().isAfter(LocalDateTime.now().minusDays(4)))
        .collect(Collectors.groupingBy(Lease::getDateLeased));

    //Makes the array that shall be returned
    int[] totalByDate = new int[5];
    Object[] dates = leasesMappedPerDay.keySet().toArray();

    //Iterates for 5 days
    for (int i = 0; i < dates.length; i++) {
      totalByDate[i] = leasesMappedPerDay.get(dates[i]).size();
    }

    return totalByDate;
  }

  /**
   * Works out amount of leases per week for last 5 weeks.
   *
   * @param leases Records of what users borrowed
   * @return Array of amount of leases for user for last 5 weeks
   */
  private static int[] getSpecificUserStatsWeek(List<Lease> leases) {
    //Sorts leases in order of Date Leased.
    leases.sort(comparing(Lease::getDateLeased));

    //Makes the array that shall be returned
    int[] totalByWeek = new int[5];

    //Iterates for 5 weeks
    for (int i = 0; i < 5; i++) {
      //Sets the date range for that week
      LocalDateTime dateTo = LocalDateTime.now().minusDays(i * 7);
      LocalDateTime dateFrom = LocalDateTime.now().minusDays((i + 1) * 7);

      //Groups all Leases by Date Leased.
      //It then filters out any leases that wasn't between start and end of each week.
      Map<LocalDateTime, List<Lease>> leasesMappedPerWeek = leases.stream()
          .filter(item -> (item.getDateLeased().isBefore(dateTo)) && (item.getDateLeased()
              .isAfter(dateFrom))).collect(Collectors.groupingBy(Lease::getDateLeased));

      int totalNoOfLeases = 0;
      Object[] dates = leasesMappedPerWeek.keySet().toArray();
      int dateSize = dates.length;

      // Iterates through the leases for each Day
      for (int j = 0; j < dateSize; j++) {
        totalNoOfLeases = totalNoOfLeases + leasesMappedPerWeek.get(dates[j]).size();
      }

      totalByWeek[i] = totalNoOfLeases;
    }

    return totalByWeek;
  }

  /**
   * Works out amount of leases per month for last 5 months.
   *
   * @param leases Records of what users borrowed
   * @return Array of amount of leases for user for last 5 months
   */
  private static int[] getSpecificUserStatsMonth(List<Lease> leases) {
    //Sorts leases in order of Date Leased.
    leases.sort(comparing(Lease::getDateLeased));

    //Makes the array that shall be returned
    int[] totalByMonth = new int[5];

    //Iterates for 5 months
    for (int i = 0; i < 5; i++) {
      //Sets the date range for that month
      LocalDateTime dateTo = LocalDateTime.now().minusMonths(i);
      LocalDateTime dateFrom = LocalDateTime.now().minusMonths(i + 1);

      //Groups all Leases by Borrowing Customer, then by Date Leased.
      //It then filters out any leases that wasn't between the start and end of each month.
      Map<LocalDateTime, List<Lease>> leasesMappedPerMonth = leases.stream()
          .filter(item -> (item.getDateLeased().isBefore(dateTo)) && (item.getDateLeased()
              .isAfter(dateFrom))).collect(Collectors.groupingBy((Lease::getDateLeased)));

      int totalNoOfLeases = 0;
      Object[] dates = leasesMappedPerMonth.keySet().toArray();
      int dateSize = dates.length;

      //Iterates through each Date
      for (int j = 0; j < dateSize; j++) {
        totalNoOfLeases = totalNoOfLeases + leasesMappedPerMonth.get(dates[j]).size();
      }

      totalByMonth[i] = totalNoOfLeases;
    }

    return totalByMonth;
  }

  /**
   * Works out amount of leases per User per day for last 5 days.
   *
   * @param leases Records of what users borrowed
   * @param customerSize number of total customers registered with the library
   * @return Array of amount of leases for user for last 5 days
   */
  private static double[] getAverageUserStatsDay(List<Lease> leases, int customerSize) {
    //Iterate through leases and set it to the start of the day
    for (Lease lease : leases) {
      LocalDateTime date = lease.getDateLeased().toLocalDate().atTime(LocalTime.of(0, 0, 0));
      lease.setDateLeased(date);
    }

    //Groups all Leases by Borrowing Customer, then by Date Leased.
    //It then filters out any leases that was before 4 days ago.
    Map<LocalDateTime, Map<Customer, List<Lease>>> leasesMappedPerDay = leases.stream()
        .filter(item -> item.getDateLeased().isAfter(LocalDateTime.now().minusDays(5)))
        .sorted(comparing(Lease::getDateLeased))
        .collect(Collectors.groupingBy(Lease::getDateLeased,
            Collectors.groupingBy(Lease::getBorrowingCustomer)));

    //Makes the array that shall be returned
    double[] totalByDate = new double[5];
    Object[] dates = leasesMappedPerDay.keySet().toArray();

    //Iterates for 5 days
    for (int i = 0; i < dates.length - 1; i++) {
      double totalNoOfLeases = 0;

      Map<Customer, List<Lease>> dateMap = leasesMappedPerDay.get((LocalDateTime) dates[i]);
      Object[] customers = dateMap.keySet().toArray();

      //Iterates through leases per customer and finds number of leases per customer per day
      //Adds it to holder variable
      for (Object key : customers) {
        int temp = dateMap.get((Customer) key).size();
        //Averages number of Leases based upon no.of Customers
        totalNoOfLeases = totalNoOfLeases + temp;
      }
      totalByDate[i] = totalNoOfLeases / customerSize;

    }

    return totalByDate;
  }

  /**
   * Works out amount of leases per User per day for last 5 weeks.
   *
   * @param leases Records of what users borrowed
   * @param customerSize number of total customers registered with the library
   * @return Array of amount of leases for user for last 5 weeks
   */
  private static double[] getAverageUserStatsWeek(List<Lease> leases, int customerSize) {
    //Iterate through leases and set it to the start of the day
    for (Lease lease : leases) {
      LocalDateTime date = lease.getDateLeased().toLocalDate().atTime(LocalTime.of(0, 0, 0));
      lease.setDateLeased(date);
    }

    //Makes the array that shall be returned
    double[] totalByWeek = new double[5];

    //Iterates for 5 weeks
    for (int i = 0; i < 5; i++) {
      //Sets the date range for that week
      LocalDateTime dateTo = LocalDateTime.now().minusDays(i * 7);
      LocalDateTime dateFrom = LocalDateTime.now().minusDays((i + 1) * 7);

      //Groups all Leases by Borrowing Customer, then by Date Leased.
      //It then filters out any leases that wasn't between start and end of each week.
      Map<LocalDateTime, Map<Customer, List<Lease>>> leasesMappedPerWeek = leases.stream()
          .filter(item -> (item.getDateLeased().isBefore(dateTo)) && (item.getDateLeased()
              .isAfter(dateFrom))).sorted(comparing(Lease::getDateLeased))
          .collect(Collectors.groupingBy((Lease::getDateLeased),
              Collectors.groupingBy(Lease::getBorrowingCustomer)));

      double totalNoOfLeases = 0;
      Object[] dates = leasesMappedPerWeek.keySet().toArray();
      int dateSize = dates.length;

      // Iterates through the leases for each Day
      for (int j = 0; j < dateSize; j++) {
        Map<Customer, List<Lease>> dateMap = leasesMappedPerWeek.get(dates[j]);
        Object[] customers = dateMap.keySet().toArray();

        //Iterates through leases per customer and finds number of leases per customer per day
        //Adds it to holder variable
        for (int k = 0; k < customers.length; k++) {
          double temp = dateMap.get(customers[k]).size();
          totalNoOfLeases = totalNoOfLeases + temp;
        }

      }
      //Averages number of Leases based upon no.of Customers
      totalByWeek[i] = totalNoOfLeases / customerSize;
    }

    return totalByWeek;
  }

  /**
   * Works out amount of leases per User per day for last 5 months.
   *
   * @param leases Records of what users borrowed
   * @param customerSize number of total customers registered with the library
   * @return Array of amount of leases for user for last 5 months
   */
  private static double[] getAverageUserStatsMonth(List<Lease> leases, int customerSize) {
    //Iterate through leases and set it to the start of the day
    for (Lease lease : leases) {
      LocalDateTime date = lease.getDateLeased().toLocalDate().atTime(LocalTime.of(0, 0, 0));
      lease.setDateLeased(date);
    }

    //Makes the array that shall be returned
    double[] totalByMonth = new double[5];

    //Iterates for 5 months
    for (int i = 0; i < 5; i++) {
      //Sets the date range for that month
      LocalDateTime dateTo = LocalDateTime.now().minusMonths(i);
      LocalDateTime dateFrom = LocalDateTime.now().minusMonths(i + 1);

      //Groups all Leases by Borrowing Customer, then by Date Leased.
      //It then filters out any leases that wasn't between the start and end of each month.
      Map<LocalDateTime, Map<Customer, List<Lease>>> leasesMappedPerMonth = leases.stream()
          .filter(item -> (item.getDateLeased().isBefore(dateTo)) && (item.getDateLeased()
              .isAfter(dateFrom))).sorted(comparing(Lease::getDateLeased))
          .collect(Collectors.groupingBy((Lease::getDateLeased),
              Collectors.groupingBy(Lease::getBorrowingCustomer)));

      double totalNoOfLeases = 0;
      Object[] dates = leasesMappedPerMonth.keySet().toArray();
      int dateSize = dates.length;

      //Iterates through each Date
      for (int j = 0; j < dateSize; j++) {
        Map<Customer, List<Lease>> dateMap = leasesMappedPerMonth.get(dates[j]);
        Object[] customers = dateMap.keySet().toArray();

        //Iterates through leases per customer and finds number of leases per customer per day
        //Adds it to holder variable
        for (int k = 0; k < customers.length; k++) {
          double temp = dateMap.get(customers[k]).size();
          totalNoOfLeases = totalNoOfLeases + temp;
        }
      }
      //Averages total number of Leased by number of customers.
      totalByMonth[i] = totalNoOfLeases / customerSize;
    }
    return totalByMonth;
  }

  /**
   * Works out fine amounts per User per day for last 5 days.
   *
   * @param fines Records of User Fines
   * @return Average and Total fine amounts per User per day for last 5 days.
   */
  private static double[][] getFineStatsDay(List<Fine> fines) {
    //Iterate through leases and set it to the start of the day
    for (Fine fine : fines) {
      LocalDateTime date = fine.getDateAccrued().toLocalDate()
          .atTime(LocalTime.of(0, 0, 0));
      Lease lease = fine.getLease();
      try {
        Field dateReturned = lease.getClass().getDeclaredField("dateReturned");
        dateReturned.setAccessible(true);
        dateReturned.set(lease, date);
      } catch (IllegalAccessException | NoSuchFieldException e) {
        e.printStackTrace();
      }
    }
    fines.sort(Comparator.comparing(Fine::getDateAccrued));

    //Groups all Leases by Fined Customer, then by Date Accrued.
    //It then filters out any leases that was before 4 days ago.

    Map<LocalDateTime, List<Fine>> finesMappedPerDay = fines.stream()
        .filter(item -> item.getDateAccrued().isAfter(LocalDateTime.now().minusDays(5)))
        .collect(Collectors.groupingBy(Fine::getDateAccrued));
    Object[] keyset = finesMappedPerDay.keySet().toArray();

    //Makes the array that shall be returned
    double[][] totalByDate = new double[2][5];

    //Iterates for 5 days
    for (int i = 0; i < keyset.length - 1; i++) {

      int noOfFines = finesMappedPerDay.get((LocalDateTime) keyset[i]).size();
      double totalFineAmount = 0;
      //Gets value of each fine and adds it to cumulative fine amount
      for (Fine fine : finesMappedPerDay.get((LocalDateTime) keyset[i])) {
        totalFineAmount += (double) fine.getAmountInPounds();
      }
      totalByDate[0][i] = totalFineAmount;
      //Gets average fine amount.
      totalByDate[1][i] = totalFineAmount / noOfFines;
    }
    return totalByDate;
  }

  /**
   * Works out fine amounts per User per week for last 5 weeks.
   *
   * @param fines Records of User Fines
   * @return Average and Total fine amounts per User per day for last 5 weeks.
   */
  private static double[][] getFineStatsWeek(List<Fine> fines) {
    //Sorts fines in order of Date Accrued.
    fines.sort(comparing(Fine::getDateAccrued));

    //Makes the array that shall be returned
    double[][] totalByWeek = new double[2][5];

    //Iterates for 5 weeks
    for (int i = 0; i < 5; i++) {
      //Sets the date range for that week
      LocalDateTime dateTo = LocalDateTime.now().minusDays(i * 7);
      LocalDateTime dateFrom = LocalDateTime.now().minusDays((i + 1) * 7);

      //It filters out any fines that wasn't between start and end of each week.
      List<Fine> finesMappedPerWeek = fines.stream()
          .filter(item -> (item.getDateAccrued().isBefore(dateTo)) && (item.getDateAccrued()
              .isAfter(dateFrom))).collect(Collectors.toList());

      int noOfFines = finesMappedPerWeek.size();

      double totalFineAmount = 0.0;

      //Gets value of each fine and adds it to cumulative fine amount
      for (Fine fine : finesMappedPerWeek) {
        totalFineAmount += (double) fine.getAmountInPounds();
      }
      totalByWeek[0][i] = totalFineAmount;
      //Gets average fine amount.
      totalByWeek[1][i] = totalFineAmount / noOfFines;
    }
    return totalByWeek;
  }

  /**
   * Works out fine amounts per User per week for last 5 months.
   *
   * @param fines Records of User Fines
   * @return Average and Total fine amounts per User per day for last 5 months.
   */
  private static double[][] getFineStatsMonth(List<Fine> fines) {
    //Sorts fines in order of Date Accrued.
    fines.sort(comparing(Fine::getDateAccrued));

    //Makes the array that shall be returned
    double[][] totalByMonth = new double[2][5];

    //Iterates for 5 months
    for (int i = 0; i < 5; i++) {
      //Sets the date range for that month
      LocalDateTime dateTo = LocalDateTime.now().minusMonths(i);
      LocalDateTime dateFrom = LocalDateTime.now().minusMonths(i + 1);

      //Groups all Fines by Fined Customer, then by Date Accrued.
      //It then filters out any fines that wasn't between start and end of each month.
      List<Fine> finesMappedPerMonth = fines.stream()
          .filter(item -> (item.getDateAccrued().isBefore(dateTo)) && (item.getDateAccrued()
              .isAfter(dateFrom))).collect(Collectors.toList());

      int noOfFines = finesMappedPerMonth.size();

      double totalFineAmount = 0.0;

      //Gets value of each fine and adds it to cumulative fine amount
      for (Fine fine : finesMappedPerMonth) {
        totalFineAmount += (double) fine.getAmountInPounds();
      }
      totalByMonth[0][i] = totalFineAmount;
      //Gets average fine amount.
      totalByMonth[1][i] = totalFineAmount / noOfFines;
    }
    return totalByMonth;
  }
}
