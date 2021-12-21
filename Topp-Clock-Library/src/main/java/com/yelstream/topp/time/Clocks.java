package com.yelstream.topp.time;

import java.time.*;

/**
 * <p>
 * Utility addressing instances of {@link Clock}.
 * </p>
 * @author Morten Sabroe Mortenen
 * @version 1.0
 * @since 2021-12-19
 */
public final class Clocks {
    private Clocks() {}

    /**
     * Creates a clock that uses the best available system clock in the default zone.
     * @return Best available system clock in the default zone.
     */
    public static Clock createClockSystemInDefaultZone() {
        return Clock.systemDefaultZone();
    }

    /**
     * Creates a clock that uses the best available system clock in the UTC zone.
     * @return Best available system clock in the UTC zone.
     */
    public static Clock createClockSystemInZoneUTC() {
        return Clock.systemUTC();
    }

    /**
     * Creates a clock that uses the best available system clock in the specified zone.
     * @param zoneId  Time-zone used to convert between an instant and a date-time.
     * @return Best available system clock in the specified zone.
     */
    public static Clock createClockSystemInZone(ZoneId zoneId) {
        return Clock.system(zoneId);
    }

    /**
     * Creates a clock that always returns the same instant in the specified zone.
     * @param fixedInstant Instant used as the clock.
     * @param zoneId  Time-zone used to convert between an instant and a date-time.
     * @return Fixed clock in the specified zone.
     */
    public static Clock createClockFixedInZone(Instant fixedInstant, ZoneId zoneId) {
        return Clock.fixed(fixedInstant, zoneId);
    }

    /**
     * Creates a clock that always returns the same instant in the default zone.
     * @param fixedInstant Instant used as the clock.
     * @return Fixed clock in the specified zone.
     */
    public static Clock createClockFixedInDefaultZone(Instant fixedInstant) {
        ZoneId zoneId = ZoneId.systemDefault();
        return Clock.fixed(fixedInstant, zoneId);
    }

    /**
     * Creates a clock that always returns the same instant in the UTC zone.
     * @param fixedInstant Instant used as the clock.
     * @return Fixed clock in the UTC zone.
     */
    public static Clock createClockFixedInZoneUTC(Instant fixedInstant) {
        ZoneOffset zoneOffset = ZoneOffset.UTC;
        return Clock.fixed(fixedInstant, zoneOffset);
    }

    /**
     * Creates a clock that always returns the same instant in a specified zone.
     * @param fixedZonedDateTime Time used as the clock.
     *                           This specifies the instant together with the zone.
     * @return Fixed clock in the specified zone.
     */
    public static Clock createClockFixedAtTime(ZonedDateTime fixedZonedDateTime) {
        Instant instant = fixedZonedDateTime.toInstant();
        ZoneId zoneId = fixedZonedDateTime.getZone();
        return Clock.fixed(instant, zoneId);
    }

    /**
     * Creates a clock that always returns the same instant in a specified zone.
     * @param fixedLocalDateTime Time used as the clock.
     *                           This specifies the instant together with the zone.
     * @param zoneId Time-zone used to convert between an instant and a date-time.
     * @return Fixed clock in the specified zone.
     */
    public static Clock createClockFixedAtTime(LocalDateTime fixedLocalDateTime, ZoneId zoneId) {
        ZonedDateTime zonedDateTime = ZonedDateTime.of(fixedLocalDateTime, zoneId);
        Instant instant = zonedDateTime.toInstant();
        return Clock.fixed(instant, zoneId);
    }

    /**
     * Creates a clock that always returns the same instant in the default zone.
     * @param fixedLocalDateTime Time used as the clock.
     * @return Fixed clock in the default zone.
     */
    public static Clock createClockFixedAtTimeInDefaultZone(LocalDateTime fixedLocalDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        return createClockFixedAtTime(fixedLocalDateTime, zoneId);
    }

    /**
     * Creates a clock that starts at a specific instant specified by an offset duration relative to an existing clock.
     * @param startLocalDateTime Time for start of the offset duration.
     * @param zoneId Time-zone for the start of the offset duration.
     * @param clock Existing clock used as reference.
     * @return Fixed clock starting at an offset duration relative to an existing clock.
     */
    public static Clock createClockStartingAtTime(LocalDateTime startLocalDateTime, ZoneId zoneId, Clock clock) {
        Instant instant0 = clock.instant();
        ZonedDateTime startZonedDateTime = ZonedDateTime.of(startLocalDateTime, zoneId);
        Instant instant1 = startZonedDateTime.toInstant();
        Duration offsetDuration = Duration.between(instant0, instant1);
        return Clock.offset(clock, offsetDuration);
    }

    /**
     * Creates a clock that starts at a specific instant specified by an offset duration relative to an existing clock.
     * @param startZonedDateTime Time for start of the offset duration.
     * @param clock Existing clock used as reference.
     * @return Fixed clock starting at an offset duration relative to an existing clock.
     */
    public static Clock createClockStartingAtTime(ZonedDateTime startZonedDateTime, Clock clock) {
        Instant instant0 = clock.instant();
        Instant instant1 = startZonedDateTime.toInstant();
        Duration offsetDuration = Duration.between(instant0, instant1);
        return Clock.offset(clock, offsetDuration);
    }

    /**
     * Creates a clock that starts at a specific instant specified by an offset duration relative to the default clock.
     * @param startLocalDateTime Time for start of the offset duration.
     * @param zoneId Time-zone for the start of the offset duration.
     * @return Fixed clock starting at an offset duration relative to the default clock.
     */
    public static Clock createClockStartingAtTime(LocalDateTime startLocalDateTime, ZoneId zoneId) {
        Clock clock = Clock.system(zoneId);
        return createClockStartingAtTime(startLocalDateTime, zoneId, clock);
    }

    /**
     * Creates a clock that starts at a specific instant specified by an offset duration relative to the default clock in the default zone.
     * This uses the default system clock as reference.
     * @param startLocalDateTime Time for start of the offset duration.
     * @return Fixed clock starting at an offset duration relative to the default clock in the default zone.
     */
    public static Clock createClockStartingAtTime(LocalDateTime startLocalDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        Clock clock = Clock.system(zoneId);
        return createClockStartingAtTime(startLocalDateTime, zoneId, clock);
    }
}
