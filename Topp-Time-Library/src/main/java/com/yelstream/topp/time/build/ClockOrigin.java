package com.yelstream.topp.time.build;

import java.util.Arrays;

/**
 * <p>
 *   Reference to the origin of a "base" clock on which settings are applied.
 * </p>
 * @author Morten Sabroe Mortenen
 * @version 1.0
 * @since 2021-12-27
 */

public enum ClockOrigin {
    /**
     * System default clocks, no dedicated parameters.
     */
    SystemInDefaultZone,

    /**
     * System default clocks with reference to UCT, no dedicated parameters.
     */
    SystemInZoneUTC,

    /**
     * System default clocks with reference to a specific zone.
     * Parameters: {@code zone}
     */
    SystemInZone,

    /**
     * Fixed clocks, optionally with reference to a specific zone.
     * Parameters: One of the sets ( {@code instant}, {@code zone} ), ( {@code instant} ), ( {@code localDateTime}, {@code zoneId} ), ( {@code localDateTime} ).
     */
    Fixed,

    /**
     * Clocks starting at a specific time, optionally with reference to a specific zone.
     * Parameters: One of the sets ( {@code localDateTime}, {@code zone} ), ( {@code localDateTime} ).
     */
    StartingAtTime;

    /**
     * Gets enumeration from the textual name while matching with the case ignored.
     * @param name Name.
     * @return Enumeration.
     */
    public static ClockOrigin valueOfIgnoreCase(String name) {
        return Arrays.stream(values()).filter(value -> name.equalsIgnoreCase(value.name())).findFirst().orElse(null);
    }
}
