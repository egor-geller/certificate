package com.epam.esm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.nio.charset.StandardCharsets;

@Configuration
public class WebConfig extends AcceptHeaderLocaleResolver {

    private static final String ERROR_BUNDLE_PATH = "i18n/errors";

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasename(ERROR_BUNDLE_PATH);
        source.setDefaultEncoding(StandardCharsets.UTF_8.name());
        return source;
    }

    @Bean
    public EntityManager entityManager(EntityManagerFactory entityManagerFactory) {
        return entityManagerFactory.createEntityManager();
    }
}

