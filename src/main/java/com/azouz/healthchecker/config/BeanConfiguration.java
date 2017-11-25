package com.azouz.healthchecker.config;

import com.azouz.healthchecker.service.ServiceChecker;
import java.util.concurrent.TimeUnit;
import net.jodah.failsafe.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author mazouz
 */
@Configuration
@EnableScheduling
public class BeanConfiguration {

  private static final Logger LOG = LoggerFactory.getLogger(BeanConfiguration.class);
  @Value("${service.timeout:200}")
  int timeout;
  @Value("${service.healthy.threshold:3}")
  int healthyThreshold;
  @Value("${service.unHealth.threshold:3}")
  int unHealthyThreshold;
  @Value("${service.name:magnificentService}")
  private String name;
  @Value("${service.url:http://localhost:12345}")
  private String url;

  @Scheduled(fixedDelay = 1 * 1000) // very 1 second
  public void runJob() {
    final CircuitBreaker circuitBreaker = new CircuitBreaker()
        .withFailureThreshold(healthyThreshold)
        .withSuccessThreshold(unHealthyThreshold)
        .withDelay(1, TimeUnit.SECONDS);
    final ServiceChecker serviceChecker = new ServiceChecker(name, url, timeout, circuitBreaker);
    serviceChecker.check();
  }

}
