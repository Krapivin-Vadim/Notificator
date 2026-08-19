package org.vadim.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

@Configuration
public class PasswordEncoderConfig {


  private final String secret;
  private final int saltLength;
  private final int iterations;

  public PasswordEncoderConfig(
      @Value("${spring.pbkdf2.secret}") String secret,
      @Value("${spring.pbkdf2.salt-length}") int saltLength,
      @Value("${spring.pbkdf2.iterations}") int iterations
  ){
    this.secret = secret;
    this.saltLength = saltLength;
    this.iterations = iterations;
  }

  @Bean
  public PasswordEncoder passwordEncoder(){
    var encoder = new Pbkdf2PasswordEncoder(
        secret, saltLength, iterations,
        Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256
    );
    encoder.setEncodeHashAsBase64(true);
    return encoder;
  }
}
