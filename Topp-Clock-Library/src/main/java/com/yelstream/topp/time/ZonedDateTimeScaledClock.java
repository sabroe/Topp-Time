package com.yelstream.topp.time;

import lombok.RequiredArgsConstructor;

import java.time.*;
import java.util.function.UnaryOperator;

/**
 * <p>
 * Clock running at a speed scaled relative to a reference clock.
 * </p>
 * <p>
 * This finds scaled differences by indirect computation as durations between instances of {@link ZonedDateTime}.
 * </p>
 * @author Morten Sabroe Mortenen
 * @version 1.0
 * @since 2021-12-21
 */
@RequiredArgsConstructor
public class ZonedDateTimeScaledClock extends Clock {

    private final Clock clock;
    private final UnaryOperator<Duration> scaleOperator;
    private final ZonedDateTime timestamp0;

    public ZonedDateTimeScaledClock(Clock clock,
                                    UnaryOperator<Duration> scaleOperator) {
        this(clock, scaleOperator, ZonedDateTime.now(clock));
    }

    @Override
    public ZoneId getZone() {
        return clock.getZone();
    }

    @Override
    public Clock withZone(ZoneId zone) {
        if (zone.equals(this.getZone())) {
            return this;
        }
        return new ZonedDateTimeScaledClock(clock.withZone(zone), scaleOperator, timestamp0);
    }

    private static ZonedDateTime getScaledTimestamp(ZonedDateTime timestamp0,
                                                    ZonedDateTime timestamp1,
                                                    UnaryOperator<Duration> scaleOperator) {
        Duration duration = Duration.between(timestamp0, timestamp1);
        Duration scaledDuration = scaleOperator.apply(duration);
        return timestamp0.plus(scaledDuration);
    }

    public ZonedDateTime getScaledTimestamp(ZonedDateTime timestamp1) {
        return getScaledTimestamp(this.timestamp0, timestamp1, scaleOperator);
    }

    @Override
    public Instant instant() {
        ZonedDateTime scaledTimestamp = getScaledTimestamp(ZonedDateTime.now(clock));
        return scaledTimestamp.toInstant();
    }
}
