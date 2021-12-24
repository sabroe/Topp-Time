package com.yelstream.topp.time;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.function.UnaryOperator;

/**
 * <p>
 * Clock running at a speed scaled relative to a reference clock.
 * </p>
 * <p>
 * This finds scaled differences by indirect computation as durations between instances of {@link Instant}.
 * </p>
 * @author Morten Sabroe Mortenen
 * @version 1.0
 * @since 2021-12-21
 */
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
public class InstantScaledClock extends Clock {

    private final Clock clock;
    private final UnaryOperator<Duration> scaleOperator;
    private final Instant timestamp0;

    /**
     * Constructor.
     * @param clock Reference clock.
     * @param scaleOperator Function defining how to scale time.
     */
    public InstantScaledClock(Clock clock,
                              UnaryOperator<Duration> scaleOperator) {
        this(clock, scaleOperator, Instant.now(clock));
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
        return new InstantScaledClock(clock.withZone(zone), scaleOperator, timestamp0);
    }

    private static Instant getScaledTimestamp(Instant timestamp0,
                                              Instant timestamp1,
                                              UnaryOperator<Duration> scaleOperator) {
        Duration duration = Duration.between(timestamp0, timestamp1);
        Duration scaledDuration = scaleOperator.apply(duration);
        return timestamp0.plus(scaledDuration);
    }

    /**
     * Gets a timestamp adjusted by scaling according to the reference clock and the scale operator.
     * @param timestamp1 Timestamp relative to reference clock and to be adjusted.
     * @return Adjusted timestamp.
     */
    public Instant getScaledTimestamp(Instant timestamp1) {
        return getScaledTimestamp(this.timestamp0, timestamp1, scaleOperator);
    }

    @Override
    public Instant instant() {
        return getScaledTimestamp(Instant.now(clock));
    }
}
