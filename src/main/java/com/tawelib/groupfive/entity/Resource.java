package com.tawelib.groupfive.entity;

import com.tawelib.groupfive.runtime.SimulatedLocalDateTime;
import java.io.Serializable;
import java.time.LocalDateTime;
import javafx.scene.image.Image;

/**
 * Resource.java Resource class saves all info pertaining to a resource.
 *
 * @author Shree Desai
 * @version 1.0
 */
public abstract class Resource implements Serializable {

  private String resourceId;
  private String title;
  private int year;
  private Image thumbnailImage;
  private final ResourceType type;
  private LocalDateTime dateAdded;

  /**
   * Instantiates a new Resource.
   *
   * @param title the title
   * @param year the year
   * @param thumbnailImage the thumbnail image
   * @param type the type
   */
  public Resource(String title, int year, Image thumbnailImage, ResourceType type) {
    this.title = title;
    this.year = year;
    this.thumbnailImage = thumbnailImage;
    this.type = type;
    //this.dateAdded = SimulatedLocalDateTime.now();
    this.dateAdded = LocalDateTime.now();
  }

  /**
   * Gets id.
   *
   * @return the id
   */
  public String getResourceId() {
    return resourceId;
  }

  /**
   * Gets title.
   *
   * @return the title
   */
  public String getTitle() {
    return title;
  }

  /**
   * Sets title.
   *
   * @param title the title
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Gets year.
   *
   * @return the year
   */
  public int getYear() {
    return year;
  }

  /**
   * Sets year.
   *
   * @param year the year
   */
  public void setYear(int year) {
    this.year = year;
  }

  /**
   * Gets thumbnail image.
   *
   * @return the thumbnail image
   */
  public Image getThumbnailImage() {
    return thumbnailImage;
  }

  /**
   * Sets thumbnail image.
   *
   * @param thumbnailImage the thumbnail image
   */
  public void setThumbnailImage(Image thumbnailImage) {
    this.thumbnailImage = thumbnailImage;
  }

  /**
   * Gets type.
   *
   * @return the type
   */
  public ResourceType getType() {
    return type;
  }

  /**
   * Gets dateAdded.
   *
   * @return the dateAdded
   */
  public LocalDateTime getDateAdded() {
    return dateAdded;
  }

}
