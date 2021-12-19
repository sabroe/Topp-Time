package com.yelstream.time.build;

import lombok.extern.slf4j.Slf4j;

import java.time.Clock;

/**
 * Utility addressing instances of {@link ClockDefinition}.
 */
@Slf4j
public final class ClockDefinitions {
    private ClockDefinitions() {}

    public static final String CLOCK_SYSTEM_PROPERTY_NAME_PREFIX = "clock";
    public static final String CLOCK_ENVIRONMENT_VARIABLE_NAME_PREFIX = "clock";

    public static ClockDefinition getClockDefinitionFromEnvironment(String clockDefinitionName) {
        ClockDefinition clockDefinition = null;
        String clockSystemPropertyName = clockDefinitionName == null ? CLOCK_SYSTEM_PROPERTY_NAME_PREFIX : CLOCK_SYSTEM_PROPERTY_NAME_PREFIX + "." + clockDefinitionName;
        String declaration = System.getProperty(clockSystemPropertyName);
        log.info(String.format("Getting clock definition; declaration read from system property %s is %s!", clockSystemPropertyName, declaration));
        if (declaration == null) {
            String clockEnvironmentVariableName = clockDefinitionName == null ? CLOCK_ENVIRONMENT_VARIABLE_NAME_PREFIX : CLOCK_ENVIRONMENT_VARIABLE_NAME_PREFIX + "." + clockDefinitionName;
            declaration = System.getenv(clockEnvironmentVariableName);
            log.info(String.format("Getting clock definition; declaration read from environment variable %s is %s!", clockEnvironmentVariableName, declaration));
        }
        if (declaration != null) {
            declaration = declaration.trim();
            clockDefinition = new ClockDefinition(declaration);
        }
        return clockDefinition;
    }

    public static Clock getClockFromEnvironment(String clockDefinitionName) {
        Clock clock = null;
        ClockDefinition clockDefinition = getClockDefinitionFromEnvironment(clockDefinitionName);
        if (clockDefinition != null) {
            clock = clockDefinition.toClock();
        }
        return clock;
    }
}
