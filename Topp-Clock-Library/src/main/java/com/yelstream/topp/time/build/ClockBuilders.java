package com.yelstream.topp.time.build;

import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.Set;
import java.util.function.UnaryOperator;

/**
 * <p>
 * Utility addressing instances of {@link ClockBuilder}.
 * </p>
 * @author Morten Sabroe Mortenen
 * @version 1.0
 * @since 2021-12-19
 */
@Slf4j
public class ClockBuilders {
    private ClockBuilders() {}

    /**
     * Creates a new clock builder.
     * @param factoryName Clock builder factory name.
     * @param factoryArgumentMap Factory argument map.
     * @return Clock builder.
     */
    public static ClockBuilder of(String factoryName,
                                  Map<String, String> factoryArgumentMap) {
        log.debug("Clock builder creation from factory name {} and factory arguments {}.", factoryName, factoryArgumentMap);
        ClockBuilder.Factory factory = ClockBuilder.Factory.valueByFactoryName(factoryName);
        if (factory == null) {
            log.warn("Failure to create builder; cannot match factory name {}!", factoryName);
            throw new IllegalArgumentException(String.format("Failure to create builder; cannot match factory name %s!", factoryName));
        }
        return of(factory, factoryArgumentMap);
    }

    /**
     * Creates a new clock builder.
     * @param factory Clock builder factory.
     * @param factoryArgumentMap Factory argument map.
     * @return Clock builder.
     */
    public static ClockBuilder of(ClockBuilder.Factory factory,
                                  Map<String, String> factoryArgumentMap) {
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
                    case "offsetDuration", "offset" -> {
                        Duration offsetDuration = Duration.parse(value);
                        builder.setOffsetDuration(offsetDuration);
                    }
                    case "tickDuration", "tick" -> {
                        Duration tickDuration = Duration.parse(value);
                        builder.setTickDuration(tickDuration);
                    }
                    case "scale.multiplyBy", "multiplyBy" -> {
                        UnaryOperator<Duration> scaleOperator = builder.getScaleOperator() == null ? UnaryOperator.identity() : builder.getScaleOperator();
                        builder.setScaleOperator(duration -> scaleOperator.apply(duration).multipliedBy(Long.parseLong(value)));
                    }
                    case "scale.divideBy", "divideBy" -> {
                        UnaryOperator<Duration> scaleOperator = builder.getScaleOperator() == null ? UnaryOperator.identity() : builder.getScaleOperator();
                        builder.setScaleOperator(duration -> scaleOperator.apply(duration).dividedBy(Long.parseLong(value)));
                    }
                    case "scale.negate", "negate" -> {
                        UnaryOperator<Duration> scaleOperator = builder.getScaleOperator() == null ? UnaryOperator.identity() : builder.getScaleOperator();
                        builder.setScaleOperator(duration -> scaleOperator.apply(duration).negated());
                    }
                    case "adjustable" -> {
                        builder.setAdjustable(value==null ? Boolean.TRUE : Boolean.valueOf(value));
                    }
                    default -> throw new IllegalArgumentException(String.format("Failure to create clock builder; cannot recognize factory argument %s, factory arguments are %s.", key, factoryArgumentMap));
                }
            }
        }

        return builder;
    }
}
