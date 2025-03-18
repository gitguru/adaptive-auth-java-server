package com.apidynamics.test.server_demo;

import com.apidynamics.test.server_demo.filter.AdaptiveAuthenticationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<AdaptiveAuthenticationFilter> adaptiveAuthFilter() {
        FilterRegistrationBean<AdaptiveAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new AdaptiveAuthenticationFilter());
        
        registrationBean.addUrlPatterns("/api/*");
        registrationBean.setOrder(2);

        return registrationBean;

    }

}