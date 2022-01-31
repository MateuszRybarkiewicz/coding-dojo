package com.assignment.spring.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(
        basePackages = "com.assignment.spring.repositories"
)
@EntityScan(basePackages = "com.assignment.spring.entities")
public class PersistenceConfiguration {
}
