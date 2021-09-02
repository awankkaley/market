package com.viaje.market.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing // enable JPA Auditing for BaseTimeEntity
public class JpaAuditingConfig {

}