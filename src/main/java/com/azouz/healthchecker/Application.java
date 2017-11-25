package com.azouz.healthchecker;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author mazouz
 */
@SpringBootApplication
public class Application {

  public static void main(final String... args) {
    SpringApplication.run(Application.class);
  }
}
