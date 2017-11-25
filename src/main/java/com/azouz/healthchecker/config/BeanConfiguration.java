package com.azouz.healthchecker.config;

import com.azouz.healthchecker.service.ServiceChecker;
import java.util.concurrent.TimeUnit;
import net.jodah.failsafe.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
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

  @Autowired
  public ServiceChecker serviceChecker;

  @Scheduled(fixedDelay = 1 * 1000) // very 1 second
  public void runJob() {
    serviceChecker.check();
  }

  @Bean
  public ServiceChecker serviceChecker(
      @Value("${service.name:magnificentService}") final String name,
      @Value("${service.url:http://localhost:12345}") final String url,
      @Value("${service.timeout:200}") final int timeout,
      @Value("${service.healthy.threshold:3}") final int healthyThreshol,
      final CircuitBreaker circuitBreaker) {
    return new ServiceChecker(name, url, timeout, circuitBreaker);
  }

  @Bean
  public CircuitBreaker circuitBreaker(
      @Value("${service.healthy.threshold:3}") final int healthyThreshol,
      @Value("${service.unHealth.threshold:3}") final int unHealthyThreshold) {
    return new CircuitBreaker()
        .withFailureThreshold(healthyThreshol)
        .withSuccessThreshold(unHealthyThreshold)
        .withDelay(1, TimeUnit.SECONDS);
  }

}
