package com.yelstream.time.build;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yelstream.time.Clocks;

/**
 *
 * @author Morten Sabroe Mortenen
 * @version 1.0
 * @since 2021-12-19
 */
@Slf4j
@Setter
@Getter
public final class DefaultClockParser implements ClockParser {
    public DefaultClockParser() {
    }

    private static final String FACTORY_NAME_REGEX = "([\\w]+)";
    private static final String ARGUMENT_NAME_REGEX = "([\\w]+)";
    private static final String ARGUMENT_VALUE_REGEX = "([\\p{Print}&&[^,]]*)";
    private static final String ARGUMENT_REGEX = ARGUMENT_NAME_REGEX + "=" + ARGUMENT_VALUE_REGEX;

    public static final String CLOCK_DEFINITION_REGEX = "^" + namedGroup("factoryName", FACTORY_NAME_REGEX) + "([(]" + namedGroup("factoryArguments", ARGUMENT_REGEX + "([,]" + ARGUMENT_REGEX + ")*") + "?" + "[)])?" + "$";
    public static final Pattern CLOCK_DEFINITION_PATTERN = Pattern.compile(CLOCK_DEFINITION_REGEX);

    private static final String ARGUMENT_ARGUMENTS_REGEX = namedGroup("argument", namedGroup("name", ARGUMENT_NAME_REGEX) + "=" + namedGroup("value", ARGUMENT_VALUE_REGEX));
    private static final Pattern ARGUMENT_PATTERN = Pattern.compile(ARGUMENT_ARGUMENTS_REGEX);

    private static String namedGroup(String group, String regEx) {
        return String.format("(?<%s>%s)", group, regEx);
    }

    @Override
    public Clock parse(ClockDefinition clockDefinition) {
        log.debug("Clock builder creation from clock definition {}.", clockDefinition);
        String declaration = clockDefinition.getDeclaration();
        ClockBuilder clockBuilder = null;
        Matcher matcher = CLOCK_DEFINITION_PATTERN.matcher(declaration);
        if (!matcher.matches()) {
            log.warn("Failure to parse; cannot match syntax of clock definition {}!", clockDefinition);
            throw new IllegalArgumentException(String.format("Failure to parse; cannot syntax of match clock definition %s!", clockDefinition));
        } else {
            String factoryName = matcher.group("factoryName");
            String factoryArguments = matcher.group("factoryArguments");
            log.debug("Clock builder creation from clock definition {} read factory name {} and factory arguments {}.", clockDefinition, factoryName, factoryArguments);
            Map<String, String> factoryArgumentMap = null;
            if (factoryArguments != null) {
                factoryArgumentMap = parseFactoryArguments(factoryArguments);
            }
            clockBuilder = of(factoryName, factoryArgumentMap);
        }
        log.info("Parsing of clock definition {} completed.", clockDefinition);
        return clockBuilder.build();
    }

    private static Map<String, String> parseFactoryArguments(String factoryArguments) {
        Map<String, String> factoryArgumentMap = new LinkedHashMap<>();  //Yes, keep the keys in the order they were inserted!
        Matcher matcher = ARGUMENT_PATTERN.matcher(factoryArguments);
        int start = 0;
        while (matcher.find(start)) {
            String name = matcher.group("name");
            String value = matcher.group("value");
            factoryArgumentMap.put(name, value);
            start = matcher.end();
        }
        return factoryArgumentMap;
    }

    public static ClockBuilder of(String factoryName, Map<String, String> factoryArgumentMap) {
        log.debug("Clock builder creation from factory name {} and factory arguments {}.", factoryName, factoryArgumentMap);
        ClockBuilder.Factory factory = ClockBuilder.Factory.valueByFactoryName(factoryName);
        if (factory == null) {
            log.warn("Failure to create builder; cannot match factory name {}!", factoryName);
            throw new IllegalArgumentException(String.format("Failure to create builder; cannot match factory name %s!", factoryName));
        }
        return of(factory, factoryArgumentMap);
    }

    public static ClockBuilder of(ClockBuilder.Factory factory, Map<String, String> factoryArgumentMap) {
        log.debug("Clock builder creation from factory {} and factory arguments {}.", factory, factoryArgumentMap);

        ClockBuilder builder = ClockBuilder.newInstance();
        builder.setFactory(factory);

        if (factoryArgumentMap != null) {
            Set<String> keySet = factoryArgumentMap.keySet();
            for (String key: keySet) {
                String value = factoryArgumentMap.get(key);

                switch (key) {
                    case "zoneId" -> {
                        ZoneId zoneId = ZoneId.of(value);
                        builder.setZoneId(zoneId);
                    }
                    case "instant" -> {
                        Instant instant = Instant.parse(value);
                        builder.setInstant(instant);
                    }
                    case "localDateTime" -> {
                        LocalDateTime localDateTime = LocalDateTime.parse(value);
                        builder.setLocalDateTime(localDateTime);
                    }
                    case "offsetDuration" -> {
                        Duration offsetDuration = Duration.parse(value);
                        builder.setOffsetDuration(offsetDuration);
                    }
                    case "tickDuration" -> {
                        Duration tickDuration = Duration.parse(value);
                        builder.setTickDuration(tickDuration);
                    }
                    default -> throw new IllegalArgumentException(String.format("Failure to create clock builder; cannot recognize factory argument %s, factory arguments are %s.", key, factoryArgumentMap));
                }
            }
        }

        return builder;
    }
}
