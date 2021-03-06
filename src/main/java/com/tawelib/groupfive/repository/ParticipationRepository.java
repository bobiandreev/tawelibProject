package com.tawelib.groupfive.repository;

import com.tawelib.groupfive.entity.Event;
import com.tawelib.groupfive.entity.Participation;
import com.tawelib.groupfive.entity.User;

import java.util.ArrayList;
import java.util.List;

/**
 * ParticipationRepository.java  the Participation repository that handles details of who is
 * planning to attend which event
 *
 * @author Rimantas Kazlauskas
 * @version 1.0
 */
public class ParticipationRepository implements BaseRepository<Participation> {

  private ArrayList<Participation> participation;

  /**
   * Instantiates a new Participation repository.
   */
  public ParticipationRepository() {
    participation = new ArrayList<>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Participation> getAll() {
    return participation;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void add(Participation participation) {
    if (!this.participation.contains(participation)) {
      this.participation.add(participation);
    }
  }

  /**
   * Gets number of participants.
   *
   * @param event the event
   * @return the number of participants
   */
  public int getNumberOfParticipants(Event event) {
    int count = 0;
    for (Participation i : participation) {
      if (event.getEventId().equals(i.getEvent().getEventId())) {
        count++;
      }
    }
    return count;

  }

  /**
   * Checks whatever a given user signed up for attending an event.
   *
   * @param event the event
   * @param user the user
   * @return true if attending, false otherwise
   */
  public boolean doesParticipate(Event event, User user) {
    boolean result = false;

    for (Participation i : participation) {
      if (event.getEventId().equals(i.getEvent().getEventId())) {
        if (user.getUsername().equals(i.getUser().getUsername())) {
          result = true;
        }
      }
    }
    return result;
  }

  /**
   * The method removes the user from current event participation repository.
   * @param event the event user is leaving
   * @param user the user wawnts to leave
   */
  public void removeParticipation(Event event, User user) {
    for (int i = 0; i < participation.size(); i++) {
      Participation part = participation.get(i);
      if ((part.getEvent() == event) && (part.getUser() == user)) {
        participation.remove(i);
        return;
      }
    }
  }


}
