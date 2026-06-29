package org.vadim.controller;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthController {

  @GetMapping("")
  ResponseEntity<Map<String, String>> getHealth(){
    return ResponseEntity.ok(Map.of("status", "ok"));
  }
}
