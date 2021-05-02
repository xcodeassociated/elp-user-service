package com.xcodeassociated.service;

import com.xcodeassociated.commons.config.audit.AuditConfig;
import com.xcodeassociated.commons.config.security.SecurityConfig;
import com.xcodeassociated.commons.correlationid.config.CorrelationIdConfig;
import com.xcodeassociated.commons.correlationid.config.CorrelationIdFilterConfig;
import com.xcodeassociated.commons.correlationid.config.CorrelationIdKafkaConfig;
import com.xcodeassociated.commons.correlationid.config.CorrelationIdRetrofitConfig;
import com.xcodeassociated.commons.envers.EnversConfig;
import com.xcodeassociated.commons.keycloak.KeycloakConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component
@Slf4j
class ServiceApplicationRunner implements CommandLineRunner {

	Environment env;

	public ServiceApplicationRunner(Environment env) {
		this.env = env;
	}

	@Override
	public void run(String... args) {
		log.info("ServiceApplicationRunner::run with: {}", args);

		Map<String, Object> map = new HashMap();
		for(Iterator it = ((AbstractEnvironment) env).getPropertySources().iterator(); it.hasNext(); ) {
			PropertySource propertySource = (PropertySource) it.next();
			if (propertySource instanceof MapPropertySource) {
				map.putAll(((MapPropertySource) propertySource).getSource());
			}
		}

		log.info(">>> env: {}", map);
	}

}

@EnableDiscoveryClient
@EnableJpaRepositories("com.xcodeassociated.service")
@EntityScan("com.xcodeassociated.service")
@Import({
		AuditConfig.class,
		EnversConfig.class,
		SecurityConfig.class,
		CorrelationIdConfig.class,
		CorrelationIdRetrofitConfig.class,
		CorrelationIdFilterConfig.class,
		CorrelationIdKafkaConfig.class,
		KeycloakConfig.class
})
@SpringBootApplication
public class ServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceApplication.class, args);
	}

}
