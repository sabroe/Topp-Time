package com.yelstream.topp.time.build;

import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.Set;

/**
 * Utility addressing instances of {@link ClockBuilder}.
 * @author Morten Sabroe Mortenen
 * @version 1.0
 * @since 2021-12-19
 */
@Slf4j
public class ClockBuilders {
    private ClockBuilders() {}

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
