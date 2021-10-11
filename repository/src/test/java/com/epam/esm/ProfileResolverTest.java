package com.epam.esm;

import org.springframework.core.env.AbstractEnvironment;
import org.springframework.test.context.ActiveProfilesResolver;

public class ProfileResolverTest implements ActiveProfilesResolver {

    private static final String TEST_PARAMETER = "test";

    @Override
    public String[] resolve(Class<?> testClass) {
        System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, TEST_PARAMETER);
        return new String[] {TEST_PARAMETER};
    }
}
