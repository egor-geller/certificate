package com.epam.esm.config;

import com.epam.esm.controller.converter.StringToSortByAscDescConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.nio.charset.StandardCharsets;

@Configuration
@EnableWebMvc
@ComponentScan("com.epam.esm")
public class WebConfig extends AcceptHeaderLocaleResolver implements WebMvcConfigurer {

    private static final String ERROR_BUNDLE_PATH = "i18n/errors";

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasename(ERROR_BUNDLE_PATH);
        source.setDefaultEncoding(StandardCharsets.UTF_8.name());
        return source;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToSortByAscDescConverter());
    }
}

