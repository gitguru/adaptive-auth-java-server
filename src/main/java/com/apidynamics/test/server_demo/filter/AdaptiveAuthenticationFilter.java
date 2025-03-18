package com.apidynamics.test.server_demo.filter;

import com.apidynamics.test.server_demo.model.ApiDynamicsClient;
import com.apidynamics.test.server_demo.service.AdaptiveAuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

// @Component
// @Order(2)
// This filter is initialized in class FilterConfig
// This is the main filter that handles Adaptive Authentication
// It is mapped to all API calls under this patter "/api/*"
public class AdaptiveAuthenticationFilter implements Filter {

  private final static Logger LOG = LoggerFactory.getLogger(AdaptiveAuthenticationFilter.class);

  private AdaptiveAuthenticationService adaptiveAuthenticationService;

  @Override
  public void init(final FilterConfig filterConfig) throws ServletException {
    LOG.info("Initializing adaptive auth filter :{}", this);
    // Doing this because this filter is initialized manually in FilterConfig.java due to custom configuration
    adaptiveAuthenticationService = WebApplicationContextUtils.getRequiredWebApplicationContext(filterConfig.getServletContext()).getBean(AdaptiveAuthenticationService.class);
  }

  /**
   * Get request headers from current request context
   * @return - HttpHeaders object
   */
  private HttpHeaders getRequestHeaders(HttpServletRequest request) {
    HttpHeaders httpHeaders = Collections.list(request.getHeaderNames())
            .stream()
            .collect(Collectors.toMap(
                    Function.identity(),
                    h -> Collections.list(request.getHeaders(h)),
                    (oldValue, newValue) -> newValue,
                    HttpHeaders::new
            ));

    return httpHeaders;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

    HttpServletRequest req = (HttpServletRequest) request;
    LOG.info("Starting adaptive auth validation for req : {}",  req.getRequestURI());

    ApiDynamicsClient apiDynamicsClient = new ApiDynamicsClient(
            req.getHeader("X-API-Dynamics-Client-Id"),
            getRequestHeaders(req).asSingleValueMap(),
            req.getRequestURI(),
            req.getMethod()
    );

    Pair<HttpStatusCode, Map<String, Object>> adaptiveValidationResult = adaptiveAuthenticationService.validateAdaptiveApiClient(apiDynamicsClient);
    HttpStatusCode httpStatusCode = adaptiveValidationResult.getFirst();

    LOG.info("Adaptive Authentication Validation status : {}", httpStatusCode);

    if (httpStatusCode.is2xxSuccessful()) {
      chain.doFilter(request, response);
    } else {
      ObjectMapper mapper = new ObjectMapper();
      HttpServletResponse res = (HttpServletResponse) response;
      res.setStatus(httpStatusCode.value());
      res.setContentType(MediaType.APPLICATION_JSON_VALUE);
      mapper.writeValue(res.getWriter(), adaptiveValidationResult.getSecond());
    }

    LOG.info("Finishing an adaptive auth validation for req : {}", req.getRequestURI());
  }

  @Override
  public void destroy() {
    LOG.warn("Destructing adaptive auth filter : {}", this);
  }

}
