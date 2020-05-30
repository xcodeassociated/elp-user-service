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
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

@Component
@Slf4j
class ServiceApplicationRunner implements CommandLineRunner {

	@Override
	public void run(String... args) {
		log.info("ServiceApplicationRunner::run with: {}", args);
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
