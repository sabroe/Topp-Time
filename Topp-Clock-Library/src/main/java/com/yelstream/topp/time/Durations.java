package com.yelstream.topp.time;

import lombok.extern.slf4j.Slf4j;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * <p>
 * Utility addressing instances of {@link Duration}.
 * </p>
 * @author Morten Sabroe Mortenen
 * @version 1.0
 * @since 2021-12-19
 */
@Slf4j
public final class Durations {
    private Durations() {}

    /**
     * Gets the duration between the end of a specific date and the current time.
     * @param startLocalDateExclusive Date for the start of the duration, exclusive.
     * @param clock Clock defining the current time.
     * @return Duration.
     */
    public static Duration getDurationFromStartDateExclusive(LocalDate startLocalDateExclusive,
                                                             Clock clock) {
        LocalDateTime startLocalDateTimeInclusive = startLocalDateExclusive.plusDays(1).atStartOfDay();
        ZonedDateTime startZonedDateTimeInclusive = ZonedDateTime.of(startLocalDateTimeInclusive, clock.getZone());
        return getDuration(startZonedDateTimeInclusive, clock);
    }

    /**
     * Gets the duration between the start of a specific date and the current time.
     * @param startZonedDateTimeInclusive Timestamp for the start of the duration, inclusive.
     * @param clock Clock defining the current time.
     * @return Duration.
     */
    public static Duration getDuration(ZonedDateTime startZonedDateTimeInclusive,
                                       Clock clock) {
        ZonedDateTime endZonedDateTimeExclusive = ZonedDateTime.now(clock);
        return Duration.between(startZonedDateTimeInclusive, endZonedDateTimeExclusive);
    }

    /**
     * Gets the duration between the start of a specific date and a specific instant.
     * @param startLocalDateExclusive Date for the start of the duration, inclusive.
     * @param zoneId Zone associated with the date for the start of the duration.
     * @param endInstantExclusive Instant for the end of the duration, exclusive.
     * @return Duration.
     */
    public static Duration getDurationFromStartDateExclusive(LocalDate startLocalDateExclusive,
                                                             ZoneId zoneId,
                                                             Instant endInstantExclusive) {
        LocalDateTime startLocalDateTimeInclusive = startLocalDateExclusive.plusDays(1).atStartOfDay();
        ZonedDateTime startZonedDateTimeInclusive = ZonedDateTime.of(startLocalDateTimeInclusive, zoneId);
        return getDuration(startZonedDateTimeInclusive, endInstantExclusive);
    }

    /**
     * Gets the duration between the start of a specific date and a specific instant.
     * @param startZonedDateTimeInclusive Timestamp for the start of the duration, inclusive.
     * @param endInstantExclusive Instant defining the end of the duration, exclusive.
     * @return Duration.
     */
    public static Duration getDuration(ZonedDateTime startZonedDateTimeInclusive,
                                       Instant endInstantExclusive) {
        ZoneId zoneId = startZonedDateTimeInclusive.getZone();
        ZonedDateTime endDateTimeExclusive = ZonedDateTime.ofInstant(endInstantExclusive, zoneId);
        return Duration.between(startZonedDateTimeInclusive, endDateTimeExclusive);
    }
}
