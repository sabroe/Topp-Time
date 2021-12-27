package com.yelstream.topp.time.build;

import com.yelstream.topp.time.AdjustableClock;
import com.yelstream.topp.time.Clocks;
import com.yelstream.topp.time.InstantScaledClock;
import com.yelstream.topp.time.declare.ClockDeclaration;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.*;
import java.util.Map;
import java.util.Set;
import java.util.function.UnaryOperator;

/**
 * <p>
 *   Configuration of a {@link Clock} instance.
 *   Configurations may be obtained by a textual declaration in the form of {@link ClockDeclaration}.
 *   The purpose of a configuration is to define a clock with a specific setup.
 * </p>
 * <p>
 *   This is immutable.
 * </p>
 * @author Morten Sabroe Mortenen
 * @version 1.0
 * @since 2021-12-27
 */
@Slf4j
@AllArgsConstructor
@Getter
@Builder(builderClassName="Builder", toBuilder=true)
public final class ClockConfiguration {

    private final ClockOrigin origin;
    private final ZoneId zone;
    private final Instant instant;
    private final LocalDateTime localDateTime;
    private final Duration offsetDuration;
    private final Duration tickDuration;
    private final UnaryOperator<Duration> scaleOperator;
    private final Boolean adjustable;

    private Clock createBaseClock() {
        Clock baseClock;
        switch (origin) {
            case SystemInDefaultZone -> {
                baseClock=Clocks.createClockSystemInDefaultZone();
            }
            case SystemInZoneUTC -> {
                baseClock=Clocks.createClockSystemInZoneUTC();
            }
            case SystemInZone -> {
                baseClock=Clocks.createClockSystemInZone(zone);
            }
            case Fixed -> {
                if (instant!=null && localDateTime!=null) {
                    throw new IllegalStateException(String.format("Failure to create clock builder; base clock origin is %s, values 'instant' and 'localDateTime' may not both be set!",origin));
                }
                if (instant!=null) {
                    if (zone!=null) {
                        baseClock=Clocks.createClockFixedInZone(instant, zone);
                    } else {
                        baseClock=Clocks.createClockFixedInDefaultZone(instant);
                    }
                } else {
                    if (zone !=null) {
                        baseClock=Clocks.createClockFixedAtTime(localDateTime, zone);
                    } else {
                        baseClock=Clocks.createClockFixedAtTimeInDefaultZone(localDateTime);
                    }
                }
            }
            case StartingAtTime -> {
                if (zone !=null) {
                    baseClock=Clocks.createClockStartingAtTime(localDateTime, zone);
                } else {
                    baseClock=Clocks.createClockStartingAtTime(localDateTime);
                }
            }
            default -> {
                throw new IllegalStateException(String.format("Failure to recognize base clock origin; origin is %s!",origin));
            }
        }
        return baseClock;
    }

    private Clock decorateBaseClock(Clock baseClock) {
        Clock clock=baseClock;
        if (offsetDuration!=null) {
            clock=Clock.offset(clock, offsetDuration);
            log.debug("Modified base clock by adding the offset {}.", offsetDuration);
        }
        if (tickDuration!=null) {
            clock=Clock.tick(clock, tickDuration);
            log.debug("Modified base clock to tick in adjustments of {}.", tickDuration);
        }
        if (scaleOperator!=null) {
            if (instant == null) {
                clock=new InstantScaledClock(clock, scaleOperator);
            } else {
                clock=new InstantScaledClock(clock, scaleOperator, instant);
            }
            log.debug("Modified base clock to scale using the operator {}.", scaleOperator);
        }
        if (adjustable!=null) {
            clock=new AdjustableClock(clock);
            log.debug("Modified base clock by allowing adjustment using the direction {}.", adjustable);
        }
        return clock;
    }

    /**
     * Converts this configuration to an actual, matching clock.
     * @return Matching clock.
     */
    public Clock toClock() {
        Clock clock=createBaseClock();
        clock=decorateBaseClock(clock);
        return clock;
    }

    /**
     * Converts this configuration to an actual, matching clock declaration.
     * @return Matching clock.
     */
    public ClockDeclaration toClockDeclaration() {
        return null;  //TODO: Fix this!
    }

    /**
     * Builder of {@link ClockConfiguration} instances.
     */
    public static class Builder {
        /**
         * Apply named arguments.
         * @param argumentMap Named arguments.
         * @return Builder.
         */
        public Builder applyArguments(Map<String, String> argumentMap) {
            ClockConfiguration.applyArguments(this,argumentMap);
            return this;
        }

        /**
         * Created a builder with named arguments applied.
         * @param argumentMap Named arguments.
         * @return Builder.
         */
        public static Builder of(Map<String, String> argumentMap) {
            return builder().applyArguments(argumentMap);
        }
    }

    /**
     * Apply named arguments.
     * @param builder Builder.
     * @param argumentMap Named arguments.
     */
    private static void applyArguments(Builder builder,
                                       Map<String, String> argumentMap) {
        log.debug("Applying arguments; arguments are {}.",argumentMap);
        if (argumentMap!=null) {
            Set<String> keySet=argumentMap.keySet();
            for (String key: keySet) {
                String value=argumentMap.get(key);

                switch (key) {
                    case "origin" -> {
                        builder.origin=ClockOrigin.valueOfIgnoreCase(value);
                    }
                    case "zone", "zoneId" -> {
                        builder.zone=ZoneId.of(value);
                    }
                    case "instant" -> {
                        builder.instant=Instant.parse(value);
                    }
                    case "localDateTime" -> {
                        builder.localDateTime=LocalDateTime.parse(value);
                    }
                    case "offsetDuration", "offset" -> {
                        builder.offsetDuration=Duration.parse(value);
                    }
                    case "tickDuration", "tick" -> {
                        builder.tickDuration=Duration.parse(value);
                    }
                    case "scale.multiplyBy", "multiplyBy" -> {
                        UnaryOperator<Duration> scaleOperator=builder.scaleOperator==null?UnaryOperator.identity():builder.scaleOperator;
                        builder.scaleOperator=duration->scaleOperator.apply(duration).multipliedBy(Long.parseLong(value));
                    }
                    case "scale.divideBy", "divideBy" -> {
                        UnaryOperator<Duration> scaleOperator=builder.scaleOperator==null?UnaryOperator.identity():builder.scaleOperator;
                        builder.scaleOperator=duration->scaleOperator.apply(duration).dividedBy(Long.parseLong(value));
                    }
                    case "scale.negate", "negate" -> {
                        UnaryOperator<Duration> scaleOperator=builder.scaleOperator==null?UnaryOperator.identity():builder.scaleOperator;
                        builder.scaleOperator=duration->scaleOperator.apply(duration).negated();
                    }
                    case "adjustable" -> {
                        builder.adjustable=value==null?Boolean.TRUE:Boolean.valueOf(value);
                    }
                    default -> {
                        throw new IllegalArgumentException(String.format("Failure to recognize argument; argument has key %s and value %s, arguments are %s!",key,value,argumentMap));
                    }
                }
            }
        }
    }
}
