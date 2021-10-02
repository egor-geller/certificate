package com.epam.esm;

import org.springframework.core.env.AbstractEnvironment;
import org.springframework.test.context.ActiveProfilesResolver;

public class ProfileResolverTest implements ActiveProfilesResolver {

    @Override
    public String[] resolve(Class<?> testClass) {
        System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "test");
        return new String[] {"test"};
    }
}
