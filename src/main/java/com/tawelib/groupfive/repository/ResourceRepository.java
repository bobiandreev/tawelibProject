package com.tawelib.groupfive.repository;

import com.tawelib.groupfive.entity.Book;
import com.tawelib.groupfive.entity.Customer;
import com.tawelib.groupfive.entity.Resource;
import java.lang.reflect.Type;
import java.util.List;

public class ResourceRepository implements BaseRepository<Resource> {

  public Resource getById(String id) {
    return null;
  }

  public Book getBookByIsbn(String isbn) {
    return null;
  }

  private void generateId(Resource resource) {

  }

  public void addRequests(Resource resource, Customer customer) {

  }

  public Resource searchResources(String query, Type types) {
    return null;
  }

  public void listResources(Resource after, int limit, Type types) {

  }

  @Override
  public List<Resource> getAll() {
    return null;
  }

  @Override
  public void add(Resource entity) {

  }
}
