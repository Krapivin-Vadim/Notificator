package org.vadim.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "account")
public class Account implements UserDetails {

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

  @OneToMany(mappedBy = "account")
  List<Reminder> reminders;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of();
  }

  @Override
  public @Nullable String getPassword() {
    return getPasswordHash();
  }
}
