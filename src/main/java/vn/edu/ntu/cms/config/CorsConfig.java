package vn.edu.ntu.cms.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Value("${app.cors.allowed-origins}")
    private String allowedOrigins;

    @Value("${app.cors.allowed-methods}")
    private String allowedMethods;

    @Value("${app.cors.allowed-headers}")
    private String allowedHeaders;

    @Value("${app.cors.exposed-headers}")
    private String exposedHeaders;

    @Value("${app.cors.allow-credentials}")
    private boolean allowCredentials;

    @Value("${app.cors.max-age}")
    private long maxAge;

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Allow specified origins
        if ("*".equals(allowedOrigins)) {
            config.addAllowedOriginPattern("*");
        } else {
            for (String origin : allowedOrigins.split(",")) {
                config.addAllowedOrigin(origin.trim());
            }
        }

        // Allow specified methods
        for (String method : allowedMethods.split(",")) {
            config.addAllowedMethod(method.trim());
        }

        // Allow specified headers
        for (String header : allowedHeaders.split(",")) {
            config.addAllowedHeader(header.trim());
        }

        // Expose specified headers
        for (String header : exposedHeaders.split(",")) {
            config.addExposedHeader(header.trim());
        }

        config.setAllowCredentials(allowCredentials);
        config.setMaxAge(maxAge);

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
} 