package org.vadim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication()
@EnableJpaRepositories(basePackages = "org.vadim.repository")
@EnableTransactionManagement()
@ComponentScan(basePackages = "org.vadim")
@EntityScan(basePackages = "org.vadim.entity")
public class NotificationService {
  public static void main(String[] args) {
    SpringApplication.run(NotificationService.class, args);
  }
}