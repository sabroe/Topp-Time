package com.yelstream.topp.time.declare;

import lombok.extern.slf4j.Slf4j;

import java.time.Clock;

/**
 * Utility addressing instances of {@link ClockDeclaration}.
 * @author Morten Sabroe Mortenen
 * @version 1.0
 * @since 2021-12-19
 */
@Slf4j
public final class ClockDeclarations {
    private ClockDeclarations() {}

    public static final String CLOCK_SYSTEM_PROPERTY_NAME_PREFIX = "clock";
    public static final String CLOCK_ENVIRONMENT_VARIABLE_NAME_PREFIX = "clock";

    private static String namedGroup(String group, String regEx) {
        return String.format("(?<%s>%s)", group, regEx);
    }

    public static ClockDeclaration getClockDefinitionFromEnvironment(String clockDefinitionName) {
        ClockDeclaration clockDeclaration = null;
        String clockSystemPropertyName = clockDefinitionName == null ? CLOCK_SYSTEM_PROPERTY_NAME_PREFIX : CLOCK_SYSTEM_PROPERTY_NAME_PREFIX + "." + clockDefinitionName;
        String declaration = System.getProperty(clockSystemPropertyName);
        log.info(String.format("Getting clock declaration; declaration read from system property %s is %s!", clockSystemPropertyName, declaration));
        if (declaration == null) {
            String clockEnvironmentVariableName = clockDefinitionName == null ? CLOCK_ENVIRONMENT_VARIABLE_NAME_PREFIX : CLOCK_ENVIRONMENT_VARIABLE_NAME_PREFIX + "." + clockDefinitionName;
            declaration = System.getenv(clockEnvironmentVariableName);
            log.info(String.format("Getting clock declaration; declaration read from environment variable %s is %s!", clockEnvironmentVariableName, declaration));
        }
        if (declaration != null) {
            declaration = declaration.trim();
            clockDeclaration = new ClockDeclaration(declaration);
        }
        return clockDeclaration;
    }

    public static Clock getClockFromEnvironment(String clockDefinitionName) {
        Clock clock = null;
        ClockDeclaration clockDeclaration = getClockDefinitionFromEnvironment(clockDefinitionName);
        if (clockDeclaration != null) {
            clock = clockDeclaration.toClock();
        }
        return clock;
    }
}
