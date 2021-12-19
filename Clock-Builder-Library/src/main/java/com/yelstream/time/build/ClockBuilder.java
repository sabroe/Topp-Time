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
public final class ClockBuilder {
    private ClockBuilder() { }

    public enum Factory {
        /**
         * System default clocks, no dedicated parameters.
         */
        SystemInDefaultZone("systemInDefaultZone", builder -> Clocks.createClockSystemInDefaultZone()),

        /**
         * System default clocks with reference to UCT, no dedicated parameters.
         */
        SystemInZoneUTC("systemInZoneUTC",  builder -> Clocks.createClockSystemInZoneUTC()),

        /**
         * System default clocks with reference to a specific zone.
         * Parameters: {@link #zoneId}
         */
        SystemInZone("systemInZone",  builder -> Clocks.createClockSystemInZone(builder.getZoneId())),

        /**
         * Fixed clocks, optionally with reference to a specific zone.
         * Parameters: One of the sets ( {@link #instant}, {@link #zoneId} ), ( {@link #instant} ), ( {@link #localDateTime}, {@link #zoneId} ), ( {@link #localDateTime} ).
         */
        Fixed("fixed",  builder -> {
            Clock clock;
            if (builder.instant != null && builder.localDateTime != null) {
                throw new IllegalStateException(String.format("Failure to create clock builder; for factory %s, values 'instant' and 'localDateTime' may not both be set!", builder.factory.factoryName));
            }
            if (builder.instant != null) {
                if (builder.zoneId != null) {
                    clock = Clocks.createClockFixedInZone(builder.instant, builder.zoneId);
                } else {
                    clock = Clocks.createClockFixedInDefaultZone(builder.instant);
                }
            } else {
                if (builder.zoneId != null) {
                    clock = Clocks.createClockFixedAtTime(builder.localDateTime, builder.zoneId);
                } else {
                    clock = Clocks.createClockFixedAtTimeInDefaultZone(builder.localDateTime);
                }
            }
            return clock;
        }),

        /**
         * Clocks starting at a specific time, optionally with reference to a specific zone.
         * Parameters: One of the sets ( {@link #localDateTime}, {@link #zoneId} ), ( {@link #localDateTime} ).
         */
        StartingAtTime("startingAtTime", builder -> {
            Clock clock;
            if (builder.zoneId != null) {
                clock = Clocks.createClockStartingAtTime(builder.localDateTime, builder.zoneId);
            } else {
                clock = Clocks.createClockStartingAtTime(builder.localDateTime);
            }
            return clock;
        });

        Factory(final String factoryName, final Function<ClockBuilder, Clock> creator) {
            this.factoryName = factoryName;
            this.creator = creator;
        }

        private final String factoryName;
        private final Function<ClockBuilder, Clock> creator;

        public String factoryName() {
            return factoryName;
        }

        public Clock createClock(final ClockBuilder builder) {
            Clock clock = creator.apply(builder);
            clock = decorateBaseClock(builder, clock);
            return clock;
        }

        private Clock decorateBaseClock(final ClockBuilder builder, final Clock baseClock) {
            Clock clock = baseClock;
            if (builder.offsetDuration != null) {
                clock = Clock.offset(clock, builder.offsetDuration);
                log.debug("Modified base clock by adding the offset {}.", builder.offsetDuration);
            }
            if (builder.tickDuration != null) {
                clock = Clock.tick(clock, builder.tickDuration);
                log.debug("Modified base clock to tick in adjustments of {}.", builder.tickDuration);
            }
            return clock;
        }

        public static Factory valueByFactoryName(final String factoryName) {
            return Arrays.stream(values()).filter(value -> factoryName.equalsIgnoreCase(value.factoryName)).findFirst().orElse(null);
        }
    }

    private Factory factory;
    private ZoneId zoneId;
    private Instant instant;
    private LocalDateTime localDateTime;
    private Duration offsetDuration;
    private Duration tickDuration;

    public Clock build() {
        return factory.createClock(this);
    }

    private static final String FACTORY_NAME_REGEX = "([\\w]+)";
    private static final String ARGUMENT_NAME_REGEX = "([\\w]+)";
    private static final String ARGUMENT_VALUE_REGEX = "([\\p{Print}&&[^,]]*)";
    private static final String ARGUMENT_REGEX = ARGUMENT_NAME_REGEX + "=" + ARGUMENT_VALUE_REGEX;

    public static final String CLOCK_DEFINITION_REGEX = "^" + namedGroup("factoryName", FACTORY_NAME_REGEX) + "([(]" + namedGroup("factoryArguments", ARGUMENT_REGEX + "([,]" + ARGUMENT_REGEX + ")*") + "?" + "[)])?" + "$";
    public static final Pattern CLOCK_DEFINITION_PATTERN = Pattern.compile(CLOCK_DEFINITION_REGEX);

    private static final String ARGUMENT_ARGUMENTS_REGEX = namedGroup("argument", namedGroup("name", ARGUMENT_NAME_REGEX) + "=" + namedGroup("value", ARGUMENT_VALUE_REGEX));
    private static final Pattern ARGUMENT_PATTERN = Pattern.compile(ARGUMENT_ARGUMENTS_REGEX);

    private static String namedGroup(final String group, final String regEx) {
        return String.format("(?<%s>%s)", group, regEx);
    }

    public static ClockBuilder newInstance() {
        return new ClockBuilder();
    }

    public static ClockBuilder parse(final String clockDefinition) {
        log.debug("Clock builder creation from clock definition {}.", clockDefinition);
        ClockBuilder clockBuilder = null;
        Matcher matcher = CLOCK_DEFINITION_PATTERN.matcher(clockDefinition);
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
            clockBuilder = ClockBuilder.of(factoryName, factoryArgumentMap);
        }
        log.info("Parsing of clock definition {} completed.", clockDefinition);
        return clockBuilder;
    }

    private static Map<String, String> parseFactoryArguments(final String factoryArguments) {
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

    public static ClockBuilder of(final String factoryName, final Map<String, String> factoryArgumentMap) {
        log.debug("Clock builder creation from factory name {} and factory arguments {}.", factoryName, factoryArgumentMap);
        Factory factory = Factory.valueByFactoryName(factoryName);
        if (factory == null) {
            log.warn("Failure to create builder; cannot match factory name {}!", factoryName);
            throw new IllegalArgumentException(String.format("Failure to create builder; cannot match factory name %s!", factoryName));
        }
        return of(factory, factoryArgumentMap);
    }

    public static ClockBuilder of(final Factory factory, final Map<String, String> factoryArgumentMap) {
        log.debug("Clock builder creation from factory {} and factory arguments {}.", factory, factoryArgumentMap);

        ClockBuilder builder = newInstance();
        builder.factory = factory;

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
