package com.yelstream.time.build;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

public class AdHocDemo {
    public static void main(final String[] args) {
        globalInstant = SystemClock.getSystemClock().instant();

        showClock("SystemClock", SystemClock.getSystemClock());

        showClock("ClockBuilder, systemInDefaultZone", ClockBuilder.parse("systemInDefaultZone").build());
        showClock("ClockBuilder, systemInZoneUTC", ClockBuilder.parse("systemInZoneUTC").build());
        showClock("ClockBuilder, systemInZone", ClockBuilder.parse("systemInZone(zoneId=Europe/London)").build());
        showClock("ClockBuilder, fixed", ClockBuilder.parse("fixed(localDateTime=2021-12-31T00:00:00,zoneId=Europe/Copenhagen)").build());
        showClock("ClockBuilder, fixed, offsetDuration", ClockBuilder.parse("fixed(localDateTime=2021-12-31T10:00:00,zoneId=Europe/Copenhagen,offsetDuration=P2D)").build());
//        showClock("ClockBuilder, fixed", ClockBuilder.parse("fixed(localDateTime=2021-12-31T00:00:00,zoneId=America/New_York)").build());
//        showClock("ClockBuilder, fixed, offsetDuration", ClockBuilder.parse("fixed(localDateTime=2021-12-31T10:00:00,zoneId=America/New_York,offsetDuration=P2D)").build());
        showClock("ClockBuilder, startingAtTime", ClockBuilder.parse("startingAtTime(localDateTime=2025-12-31T10:00:00,zoneId=Europe/Copenhagen)").build());
        showClock("ClockBuilder, startingAtTime, tickDuration", ClockBuilder.parse("startingAtTime(localDateTime=2021-12-31T10:00:00,zoneId=Europe/Copenhagen,tickDuration=PT15M)").build());
    }

    private static Instant globalInstant;

    public static void showClock(final String title, final Clock clock) {
        System.out.println("*** " + title + ": ***");
        System.out.println("Clock:                                                                                                                    " + clock);
        System.out.println("clock.getZone():                                                                                                          " + clock.getZone());
        System.out.println("clock.instant().toEpochMilli():                                                                                           " + clock.instant().toEpochMilli());
        System.out.println("clock.getZone().getRules().getDaylightSavings(Instant.from(LocalDate.parse(\"2021-01-01\").atStartOfDay(clock.getZone()))): " + clock.getZone().getRules().getDaylightSavings(Instant.from(LocalDate.parse("2021-01-01").atStartOfDay(clock.getZone()))));
        System.out.println("clock.getZone().getRules().getDaylightSavings(Instant.from(LocalDate.parse(\"2021-07-01\").atStartOfDay(clock.getZone()))): " + clock.getZone().getRules().getDaylightSavings(Instant.from(LocalDate.parse("2021-07-01").atStartOfDay(clock.getZone()))));

        System.out.println("LocalDateTime.now(clock):                                                                                                 " + LocalDateTime.now(clock));
        System.out.println("OffsetDateTime.now(clock):                                                                                                " + OffsetDateTime.now(clock));
        System.out.println("ZonedDateTime.now(clock):                                                                                                 " + ZonedDateTime.now(clock));
        System.out.println("LocalDateTime.ofInstant(globalInstant,clock.getZone())):                                                                  " + LocalDateTime.ofInstant(globalInstant, clock.getZone()));
        System.out.println("OffsetDateTime.ofInstant(globalInstant,clock.getZone())):                                                                 " + OffsetDateTime.ofInstant(globalInstant, clock.getZone()));
        System.out.println("ZonedDateTime.ofInstant(globalInstant,clock.getZone())):                                                                  " + ZonedDateTime.ofInstant(globalInstant, clock.getZone()));
        System.out.println();
    }
}
