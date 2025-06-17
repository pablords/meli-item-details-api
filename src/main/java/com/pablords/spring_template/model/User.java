package com.pablords.spring_template.model;

import java.util.UUID;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
  @Column(name = "id")
  @Id
  private String id;
  private String name;
  private String email;

  public User() {
    this.id = UUID.randomUUID().toString();
  }

  public User(String name, String email) {
    this.id = UUID.randomUUID().toString();
    this.name = name;
    this.email = email;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}