package com.azouz.healthchecker.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.util.concurrent.Callable;
import net.jodah.failsafe.CircuitBreaker;
import net.jodah.failsafe.Failsafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mazouz
 */
public class ServiceChecker {

  private static final Logger LOG = LoggerFactory.getLogger(ServiceChecker.class);

  private final String url;
  private final int timeout;
  private final CircuitBreaker circuitBreaker;
  private final String name;

  public ServiceChecker(final String name, final String url, final int timeout,
      final CircuitBreaker circuitBreaker) {
    this.url = url;
    this.timeout = timeout;
    this.circuitBreaker = circuitBreaker;
    this.name = name;
    circuitBreakerListeners();
  }

  private void executeWithCircuitBreaker(final Callable<Void> callable) {
    try{
      Failsafe.with(circuitBreaker)
          .run(() -> {
            callable.call();
          });
    }catch (final RuntimeException ex) {
      LOG.error(MessageFormat.format("failure call Service:{0}", name));
    }
  }

  public void check() {
    executeWithCircuitBreaker(() -> {
      hitService();
      return null;
    });
  }

  private void circuitBreakerListeners() {
    circuitBreaker.onOpen(() -> {
      LOG.error(MessageFormat.format("Service:{0} is unealthy", name));
    }).onClose(() -> {
      LOG.info(MessageFormat.format("Service:{0} is back again", name));
    });
  }

  public void hitService() throws RuntimeException, IOException {
    final URL urlServer = new URL(url);

    final HttpURLConnection urlConn = (HttpURLConnection) urlServer.openConnection();
    urlConn.setConnectTimeout(timeout); //<- 3Seconds Timeout
    urlConn.connect();
    if (urlConn.getResponseCode() != 200) {
      throw new RuntimeException("Failed to get response");
    }
  }
}
