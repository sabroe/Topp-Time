package com.yelstream.topp.time.declare;

import lombok.extern.slf4j.Slf4j;

import java.time.Clock;

/**
 * <p>
 *   Utility addressing instances of {@link ClockDeclaration}.
 * </p>
 * @author Morten Sabroe Mortenen
 * @version 1.0
 * @since 2021-12-19
 */
@Slf4j
public final class ClockDeclarations {
    private ClockDeclarations() {}

    /**
     * Prefix for clock declaration names used for lookup within system properties.
     */
    public static final String CLOCK_SYSTEM_PROPERTY_NAME_PREFIX = "clock";
    /**
     *
     * Prefix for clock declaration names used for lookup within environment variables.
     */
    public static final String CLOCK_ENVIRONMENT_VARIABLE_NAME_PREFIX = "clock";

    private static String namedGroup(String group, String regEx) {
        return String.format("(?<%s>%s)", group, regEx);
    }

    /**
     * Gets a clock declaration from the environment.
     * @param clockDeclarationName Clock declaration name.
     * @return Clock declaration.
     */
    public static ClockDeclaration getClockDefinitionFromEnvironment(String clockDeclarationName) {
        ClockDeclaration clockDeclaration = null;
        String clockSystemPropertyName = clockDeclarationName == null ? CLOCK_SYSTEM_PROPERTY_NAME_PREFIX : CLOCK_SYSTEM_PROPERTY_NAME_PREFIX + "." + clockDeclarationName;
        String declaration = System.getProperty(clockSystemPropertyName);
        log.info(String.format("Getting clock declaration; declaration read from system property %s is %s!", clockSystemPropertyName, declaration));
        if (declaration == null) {
            String clockEnvironmentVariableName = clockDeclarationName == null ? CLOCK_ENVIRONMENT_VARIABLE_NAME_PREFIX : CLOCK_ENVIRONMENT_VARIABLE_NAME_PREFIX + "." + clockDeclarationName;
            declaration = System.getenv(clockEnvironmentVariableName);
            log.info(String.format("Getting clock declaration; declaration read from environment variable %s is %s!", clockEnvironmentVariableName, declaration));
        }
        if (declaration != null) {
            declaration = declaration.trim();
            clockDeclaration = new ClockDeclaration(declaration);
        }
        return clockDeclaration;
    }

    /**
     * Gets a clock from the environment.
     * @param clockDeclarationName Clock declaration name.
     * @return Clock.
     */
    public static Clock getClockFromEnvironment(String clockDeclarationName) {
        Clock clock = null;
        ClockDeclaration clockDeclaration = getClockDefinitionFromEnvironment(clockDeclarationName);
        if (clockDeclaration != null) {
            clock = clockDeclaration.toClock();
        }
        return clock;
    }
}
