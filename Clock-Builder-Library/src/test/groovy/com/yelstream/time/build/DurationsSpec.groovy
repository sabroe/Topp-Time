package com.yelstream.time.build

import com.yelstream.time.Clocks
import com.yelstream.time.Durations
import spock.lang.Specification
import spock.lang.Unroll

import java.time.Clock
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

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
            '2021-01-20' | '2021-01-25T15:00' | Duration.ofDays(4).plusHours(15)
            '2021-07-20' | '2021-07-25T15:00' | Duration.ofDays(4).plusHours(15)
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
            '2021-01-20' | '2021-01-25T15:00' | Duration.ofDays(4).plusHours(15)
            '2021-07-20' | '2021-07-25T15:00' | Duration.ofDays(4).plusHours(15)
    }
}
