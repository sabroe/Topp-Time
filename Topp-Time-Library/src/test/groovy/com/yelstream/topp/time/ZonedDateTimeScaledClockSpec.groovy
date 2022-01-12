package com.yelstream.topp.time

import spock.lang.Specification
import spock.lang.Unroll

import java.time.Clock
import java.time.Duration
import java.time.ZonedDateTime

/**
 * <p>
 * Test suite addressing {@link ZonedDateTimeScaledClock}.
 * </p>
 * @author Morten Sabroe Mortenen
 * @version 1.0
 * @since 2021-12-21
 */
class ZonedDateTimeScaledClockSpec extends Specification {
    @Unroll
    void "Verify basic scaling at selected granularities"() {
        given:
            Clock clock = Clock.systemDefaultZone()
            ZonedDateTime initialTimestamp = ZonedDateTime.now(clock)

        when:
            ZonedDateTimeScaledClock scaledClock = new ZonedDateTimeScaledClock(clock, scaleOperator, initialTimestamp)
            ZonedDateTime scaledDateTime = scaledClock.getScaledTimestamp(initialTimestamp + durationToAdd)

        then:
            Duration duration = Duration.between(initialTimestamp, scaledDateTime)
            duration == expectedDuration

        where:
            _ | scaleOperator                                    | durationToAdd              || expectedDuration

            _ | { Duration d -> d.dividedBy(10000) }             | Duration.ZERO              || Duration.ZERO
            _ | { Duration d -> d.dividedBy(10000) }             | Duration.ofMillis(10000L)  || Duration.ofMillis(1)
            _ | { Duration d -> d.dividedBy(10000) }             | Duration.ofMillis(20000L)  || Duration.ofMillis(2)
            _ | { Duration d -> d.dividedBy(10000) }             | Duration.ofMillis(30000L)  || Duration.ofMillis(3)
            _ | { Duration d -> d.dividedBy(10000) }             | Duration.ofMillis(40000L)  || Duration.ofMillis(4)
            _ | { Duration d -> d.dividedBy(10000) }             | Duration.ofMillis(50000L)  || Duration.ofMillis(5)
            _ | { Duration d -> d.dividedBy(10000) }             | Duration.ofMillis(60000L)  || Duration.ofMillis(6)
            _ | { Duration d -> d.dividedBy(10000) }             | Duration.ofMillis(70000L)  || Duration.ofMillis(7)
            _ | { Duration d -> d.dividedBy(10000) }             | Duration.ofMillis(80000L)  || Duration.ofMillis(8)
            _ | { Duration d -> d.dividedBy(10000) }             | Duration.ofMillis(90000L)  || Duration.ofMillis(9)
            _ | { Duration d -> d.dividedBy(10000) }             | Duration.ofMillis(100000L) || Duration.ofMillis(10)

            _ | { Duration d -> d.dividedBy(1000) }              | Duration.ZERO              || Duration.ZERO
            _ | { Duration d -> d.dividedBy(1000) }              | Duration.ofMillis(1000L)   || Duration.ofMillis(1)
            _ | { Duration d -> d.dividedBy(1000) }              | Duration.ofMillis(2000L)   || Duration.ofMillis(2)
            _ | { Duration d -> d.dividedBy(1000) }              | Duration.ofMillis(3000L)   || Duration.ofMillis(3)
            _ | { Duration d -> d.dividedBy(1000) }              | Duration.ofMillis(4000L)   || Duration.ofMillis(4)
            _ | { Duration d -> d.dividedBy(1000) }              | Duration.ofMillis(5000L)   || Duration.ofMillis(5)
            _ | { Duration d -> d.dividedBy(1000) }              | Duration.ofMillis(6000L)   || Duration.ofMillis(6)
            _ | { Duration d -> d.dividedBy(1000) }              | Duration.ofMillis(7000L)   || Duration.ofMillis(7)
            _ | { Duration d -> d.dividedBy(1000) }              | Duration.ofMillis(8000L)   || Duration.ofMillis(8)
            _ | { Duration d -> d.dividedBy(1000) }              | Duration.ofMillis(9000L)   || Duration.ofMillis(9)
            _ | { Duration d -> d.dividedBy(1000) }              | Duration.ofMillis(10000L)  || Duration.ofMillis(10)

            _ | { Duration d -> d.dividedBy(100) }               | Duration.ZERO              || Duration.ZERO
            _ | { Duration d -> d.dividedBy(100) }               | Duration.ofMillis(1000L)   || Duration.ofMillis(10)
            _ | { Duration d -> d.dividedBy(100) }               | Duration.ofMillis(2000L)   || Duration.ofMillis(20)
            _ | { Duration d -> d.dividedBy(100) }               | Duration.ofMillis(3000L)   || Duration.ofMillis(30)
            _ | { Duration d -> d.dividedBy(100) }               | Duration.ofMillis(4000L)   || Duration.ofMillis(40)
            _ | { Duration d -> d.dividedBy(100) }               | Duration.ofMillis(5000L)   || Duration.ofMillis(50)
            _ | { Duration d -> d.dividedBy(100) }               | Duration.ofMillis(6000L)   || Duration.ofMillis(60)
            _ | { Duration d -> d.dividedBy(100) }               | Duration.ofMillis(7000L)   || Duration.ofMillis(70)
            _ | { Duration d -> d.dividedBy(100) }               | Duration.ofMillis(8000L)   || Duration.ofMillis(80)
            _ | { Duration d -> d.dividedBy(100) }               | Duration.ofMillis(9000L)   || Duration.ofMillis(90)
            _ | { Duration d -> d.dividedBy(100) }               | Duration.ofMillis(10000L)  || Duration.ofMillis(100)

            _ | { Duration d -> d.dividedBy(10) }                | Duration.ZERO              || Duration.ZERO
            _ | { Duration d -> d.dividedBy(10) }                | Duration.ofMillis(1000L)   || Duration.ofMillis(100)
            _ | { Duration d -> d.dividedBy(10) }                | Duration.ofMillis(2000L)   || Duration.ofMillis(200)
            _ | { Duration d -> d.dividedBy(10) }                | Duration.ofMillis(3000L)   || Duration.ofMillis(300)
            _ | { Duration d -> d.dividedBy(10) }                | Duration.ofMillis(4000L)   || Duration.ofMillis(400)
            _ | { Duration d -> d.dividedBy(10) }                | Duration.ofMillis(5000L)   || Duration.ofMillis(500)
            _ | { Duration d -> d.dividedBy(10) }                | Duration.ofMillis(6000L)   || Duration.ofMillis(600)
            _ | { Duration d -> d.dividedBy(10) }                | Duration.ofMillis(7000L)   || Duration.ofMillis(700)
            _ | { Duration d -> d.dividedBy(10) }                | Duration.ofMillis(8000L)   || Duration.ofMillis(800)
            _ | { Duration d -> d.dividedBy(10) }                | Duration.ofMillis(9000L)   || Duration.ofMillis(900)
            _ | { Duration d -> d.dividedBy(10) }                | Duration.ofMillis(10000L)  || Duration.ofMillis(1000)

            _ | { Duration d -> d.dividedBy(2) }                 | Duration.ZERO              || Duration.ZERO
            _ | { Duration d -> d.dividedBy(2) }                 | Duration.ofMillis(1000L)   || Duration.ofMillis(500)
            _ | { Duration d -> d.dividedBy(2) }                 | Duration.ofMillis(2000L)   || Duration.ofMillis(1000)
            _ | { Duration d -> d.dividedBy(2) }                 | Duration.ofMillis(3000L)   || Duration.ofMillis(1500)
            _ | { Duration d -> d.dividedBy(2) }                 | Duration.ofMillis(4000L)   || Duration.ofMillis(2000)
            _ | { Duration d -> d.dividedBy(2) }                 | Duration.ofMillis(5000L)   || Duration.ofMillis(2500)
            _ | { Duration d -> d.dividedBy(2) }                 | Duration.ofMillis(6000L)   || Duration.ofMillis(3000)
            _ | { Duration d -> d.dividedBy(2) }                 | Duration.ofMillis(7000L)   || Duration.ofMillis(3500)
            _ | { Duration d -> d.dividedBy(2) }                 | Duration.ofMillis(8000L)   || Duration.ofMillis(4000)
            _ | { Duration d -> d.dividedBy(2) }                 | Duration.ofMillis(9000L)   || Duration.ofMillis(4500)
            _ | { Duration d -> d.dividedBy(2) }                 | Duration.ofMillis(10000L)  || Duration.ofMillis(5000)

            _ | { Duration d -> d.multipliedBy(3).dividedBy(2) } | Duration.ZERO              || Duration.ZERO
            _ | { Duration d -> d.multipliedBy(3).dividedBy(2) } | Duration.ofMillis(1000L)   || Duration.ofMillis(1500)
            _ | { Duration d -> d.multipliedBy(3).dividedBy(2) } | Duration.ofMillis(2000L)   || Duration.ofMillis(3000)
            _ | { Duration d -> d.multipliedBy(3).dividedBy(2) } | Duration.ofMillis(3000L)   || Duration.ofMillis(4500)
            _ | { Duration d -> d.multipliedBy(3).dividedBy(2) } | Duration.ofMillis(4000L)   || Duration.ofMillis(6000)
            _ | { Duration d -> d.multipliedBy(3).dividedBy(2) } | Duration.ofMillis(5000L)   || Duration.ofMillis(7500)
            _ | { Duration d -> d.multipliedBy(3).dividedBy(2) } | Duration.ofMillis(6000L)   || Duration.ofMillis(9000)
            _ | { Duration d -> d.multipliedBy(3).dividedBy(2) } | Duration.ofMillis(7000L)   || Duration.ofMillis(10500)
            _ | { Duration d -> d.multipliedBy(3).dividedBy(2) } | Duration.ofMillis(8000L)   || Duration.ofMillis(12000)
            _ | { Duration d -> d.multipliedBy(3).dividedBy(2) } | Duration.ofMillis(9000L)   || Duration.ofMillis(13500)
            _ | { Duration d -> d.multipliedBy(3).dividedBy(2) } | Duration.ofMillis(10000L)  || Duration.ofMillis(15000)

            _ | { Duration d -> d.multipliedBy(100) }            | Duration.ZERO              || Duration.ZERO
            _ | { Duration d -> d.multipliedBy(100) }            | Duration.ofMillis(2500L)   || Duration.ofMillis(250000)
            _ | { Duration d -> d.multipliedBy(100) }            | Duration.ofMillis(5000L)   || Duration.ofMillis(500000)
            _ | { Duration d -> d.multipliedBy(100) }            | Duration.ofMillis(7500L)   || Duration.ofMillis(750000)
            _ | { Duration d -> d.multipliedBy(100) }            | Duration.ofMillis(10000L)  || Duration.ofMillis(1000000)
            _ | { Duration d -> d.multipliedBy(100) }            | Duration.ofMillis(12500L)  || Duration.ofMillis(1250000)
            _ | { Duration d -> d.multipliedBy(100) }            | Duration.ofMillis(15000L)  || Duration.ofMillis(1500000)

            _ | { Duration d -> d.multipliedBy(1000) }           | Duration.ZERO              || Duration.ZERO
            _ | { Duration d -> d.multipliedBy(1000) }           | Duration.ofMillis(2500L)   || Duration.ofMillis(2500000)
            _ | { Duration d -> d.multipliedBy(1000) }           | Duration.ofMillis(5000L)   || Duration.ofMillis(5000000)
            _ | { Duration d -> d.multipliedBy(1000) }           | Duration.ofMillis(7500L)   || Duration.ofMillis(7500000)
            _ | { Duration d -> d.multipliedBy(1000) }           | Duration.ofMillis(10000L)  || Duration.ofMillis(10000000)
            _ | { Duration d -> d.multipliedBy(1000) }           | Duration.ofMillis(12500L)  || Duration.ofMillis(12500000)
            _ | { Duration d -> d.multipliedBy(1000) }           | Duration.ofMillis(15000L)  || Duration.ofMillis(15000000)

            _ | { Duration d -> d.multipliedBy(10000) }          | Duration.ZERO              || Duration.ZERO
            _ | { Duration d -> d.multipliedBy(10000) }          | Duration.ofMillis(2500L)   || Duration.ofMillis(25000000)
            _ | { Duration d -> d.multipliedBy(10000) }          | Duration.ofMillis(5000L)   || Duration.ofMillis(50000000)
            _ | { Duration d -> d.multipliedBy(10000) }          | Duration.ofMillis(7500L)   || Duration.ofMillis(75000000)
            _ | { Duration d -> d.multipliedBy(10000) }          | Duration.ofMillis(10000L)  || Duration.ofMillis(100000000)
            _ | { Duration d -> d.multipliedBy(10000) }          | Duration.ofMillis(12500L)  || Duration.ofMillis(125000000)
            _ | { Duration d -> d.multipliedBy(10000) }          | Duration.ofMillis(15000L)  || Duration.ofMillis(150000000)
    }
}
