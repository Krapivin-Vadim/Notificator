package org.vadim.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "account")
public class Account {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @Size(max = 30)
  @Column(nullable = false, unique = true)
  String username;

  @Column(nullable = false, unique = true)
  @Size(max = 50)
  String email;

  @Size(max = 50)
  String telegram;

  @Column(name = "password_hash", nullable = false, length = 255)
  String passwordHash;
}
