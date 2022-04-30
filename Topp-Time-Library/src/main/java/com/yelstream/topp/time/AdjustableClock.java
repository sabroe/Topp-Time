package com.yelstream.topp.time;

import lombok.*;

import java.time.Clock;
import java.time.Instant;
import java.time.InstantSource;
import java.time.ZoneId;
import java.util.function.UnaryOperator;

/**
 * <p>
 *   Adjustable clock.
 *   This allows for the modification of the timestamps coming from another clock.
 * </p>
 * <p>
 *   This does not touch upon the timezone which is fixed.
 * </p>
 * @author Morten Sabroe Mortenen
 * @version 1.0
 * @since 2021-12-24
 */
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=true)
@ToString
public class AdjustableClock extends Clock {

    private final ZoneId zone;
    private final InstantSource instantSource;

    @Getter
    @Setter
    private UnaryOperator<Instant> adjustmentOperator;

    /**
     * Constructor.
     * @param clock Reference clock.
     */
    public AdjustableClock(Clock clock) {
        this(clock.getZone(), clock);
    }

    /**
     * Constructor.
     * @param clock Reference clock.
     * @param adjustmentOperator Function adjusting timestamps.
     */
    public AdjustableClock(Clock clock,
                           UnaryOperator<Instant> adjustmentOperator) {
        this(clock.getZone(), clock, adjustmentOperator);
    }

    @Override
    public ZoneId getZone() {
        return zone;
    }

    @Override
    public Clock withZone(ZoneId zone) {
        if (zone.equals(this.getZone())) {
            return this;
        }
        return new AdjustableClock(zone, instantSource, adjustmentOperator);
    }

    private static Instant getAdjustedTimestamp(Instant timestamp,
                                                UnaryOperator<Instant> adjustmentOperator) {
        Instant adjustedTimestamp=timestamp;
        if (adjustmentOperator!=null) {
            adjustedTimestamp=adjustmentOperator.apply(adjustedTimestamp);
        }
        return adjustedTimestamp;
    }

    /**
     * Gets an adjusted timestamp.
     * @param timestamp Timestamp.
     * @return Adjusted timestamp.
     */
    public Instant getAdjustedTimestamp(Instant timestamp) {
        return getAdjustedTimestamp(timestamp, adjustmentOperator);
    }

    @Override
    public Instant instant() {
        return getAdjustedTimestamp(instantSource.instant());
    }
}
