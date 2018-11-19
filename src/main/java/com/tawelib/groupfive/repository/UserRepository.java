package com.tawelib.groupfive.repository;

public interface UserRepository<UserT> extends BaseRepository<UserT> {

  UserT authenticate(String username);

  /**
   * Generates a username for a given user.
   *
   * @param user Customer or Librarian
   */
  void generateUsername(UserT user);
}
