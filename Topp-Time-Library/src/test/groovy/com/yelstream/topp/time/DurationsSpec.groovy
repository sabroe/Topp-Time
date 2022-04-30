package com.yelstream.topp.time

import spock.lang.Specification
import spock.lang.Unroll

import java.time.Clock
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * <p>
 * Test suite addressing {@link Durations}.
 * </p>
 * @author Morten Sabroe Mortenen
 * @version 1.0
 * @since 2021-12-19
 */
class DurationsSpec extends Specification {
    @Unroll
    void "Computing duration after calender date according to fixed clock"() {
        given:
            ZoneId zoneId = ZoneId.of('Europe/Copenhagen')

        when:
            LocalDate startLocalDateExclusive = LocalDate.parse(calendarDate)
            Clock clock = Clocks.createClockFixedAtTime(LocalDateTime.parse(clockStartTime), zoneId)
            Duration duration = Durations.getDurationFromStartDateExclusive(startLocalDateExclusive, clock)

        then:
            duration == expectedDuration

        where:
            calendarDate | clockStartTime     | expectedDuration
            '2022-01-20' | '2022-01-25T12:00' | Duration.ofDays(4).plusHours(12)
            '2022-07-20' | '2022-07-25T12:00' | Duration.ofDays(4).plusHours(12)
    }

    @Unroll
    void "Computing duration after calender date according to running clock"() {
        given:
            ZoneId zoneId = ZoneId.of('Europe/Copenhagen')

        when:
            LocalDate startLocalDateExclusive = LocalDate.parse(calendarDate)
            Clock clock = Clocks.createClockStartingAtTime(LocalDateTime.parse(clockStartTime), zoneId)
            Duration duration = Durations.getDurationFromStartDateExclusive(startLocalDateExclusive, clock)

        then:
            duration >= expectedDuration
            duration < expectedDuration.plusSeconds(10)

        where:
            calendarDate | clockStartTime     | expectedDuration
            '2022-01-20' | '2022-01-25T12:00' | Duration.ofDays(4).plusHours(12)
            '2022-07-20' | '2022-07-25T12:00' | Duration.ofDays(4).plusHours(12)
    }
}
