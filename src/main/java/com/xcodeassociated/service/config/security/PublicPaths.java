package com.xcodeassociated.service.config.security;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PublicPaths implements com.xcodeassociated.commons.config.security.PublicPaths {

    @Override
    public List<String> getPaths() {
        return List.of(
                "/v1/public/**",
                "/user/api/v1/public/**",
                "/user/v2/api-docs/**",
                "/user/v3/api-docs/**",
                "/user/api-docs/**",
                "/user/swagger-ui.html",
                "/user/swagger-ui/**",
                "/user/swagger-resources/**",
                "/user/webjars/**",
                "/user/configuration/**",
                "/user/favicon.ico",
                "/user/error",
                "/user/actuator/**"
        );
    }

}
