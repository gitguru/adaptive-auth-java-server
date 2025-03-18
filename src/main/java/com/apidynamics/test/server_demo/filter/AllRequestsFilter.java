package com.apidynamics.test.server_demo.filter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

@Component
@Order(1)
// This filter is for demonstration purpose only
// It only log the start and end of a request transaction for all requests to the server
public class AllRequestsFilter implements Filter {

  private final static Logger LOG = LoggerFactory.getLogger(AllRequestsFilter.class);

  @Override
  public void init(final FilterConfig filterConfig) throws ServletException {
    LOG.info("Initializing filter : {}", this);
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

    HttpServletRequest req = (HttpServletRequest) request;
    String requestData = String.format("%s %s", req.getMethod(), req.getRequestURI());
    LOG.info("Starting a transaction for req : {}",  requestData);

    chain.doFilter(request, response);

    LOG.info("Committing a transaction for req : {}", requestData);
  }

  @Override
  public void destroy() {
    LOG.warn("Destructing filter : {}", this);
  }

}
