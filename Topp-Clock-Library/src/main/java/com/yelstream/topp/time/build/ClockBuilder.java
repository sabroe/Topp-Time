package com.yelstream.topp.time.build;

import com.yelstream.topp.time.Clocks;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.*;
import java.util.Arrays;
import java.util.function.Function;

/**
 * <p>
 * Builder of {@link Clock} instances.
 * </p>
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

        Factory(String factoryName, Function<ClockBuilder, Clock> creator) {
            this.factoryName = factoryName;
            this.creator = creator;
        }

        private final String factoryName;
        private final Function<ClockBuilder, Clock> creator;

        public String factoryName() {
            return factoryName;
        }

        public Clock createClock(ClockBuilder builder) {
            Clock clock = creator.apply(builder);
            clock = decorateBaseClock(builder, clock);
            return clock;
        }

        private Clock decorateBaseClock(ClockBuilder builder, Clock baseClock) {
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

        public static Factory valueByFactoryName(String factoryName) {
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

    public static ClockBuilder newInstance() {
        return new ClockBuilder();
    }
}
