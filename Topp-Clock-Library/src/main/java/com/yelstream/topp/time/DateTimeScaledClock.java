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
class DateTimeScaledClock extends Clock {

    private final Clock clock;
    private final UnaryOperator<Duration> scaleOperator;
    private final ZonedDateTime dateTime;

    public DateTimeScaledClock(Clock clock,
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
        return new DateTimeScaledClock(clock.withZone(zone), scaleOperator, dateTime);
    }

    private static ZonedDateTime getScaledZonedDateTime(ZonedDateTime dateTime0,
                                                        ZonedDateTime dateTime1,
                                                        UnaryOperator<Duration> scaleOperator) {
        Duration duration = Duration.between(dateTime0, dateTime1);
        Duration scaledDuration = scaleOperator.apply(duration);

        return dateTime0.plus(scaledDuration);
    }

    public ZonedDateTime getScaledZonedDateTime(ZonedDateTime dateTime) {
        return getScaledZonedDateTime(this.dateTime, dateTime, scaleOperator);
    }

    @Override
    public Instant instant() {
        ZonedDateTime scaledDuration = getScaledZonedDateTime(ZonedDateTime.now(clock));
        return scaledDuration.toInstant();
    }
}
