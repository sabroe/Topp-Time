package com.yelstream.topp.time

import spock.lang.Specification
import spock.lang.Unroll

import java.time.*

/**
 * Test suite addressing {@link InstantScaledClock}.
 * @author Morten Sabroe Mortenen
 * @version 1.0
 * @since 2021-12-21
 */
class InstantScaledClockSpec extends Specification {
    @Unroll
    void "Verify basic scaling at selected granularities"() {
        given:
            Clock clock = Clock.systemDefaultZone()
            Instant initialInstant = clock.instant()

        when:
            InstantScaledClock scaledClock = new InstantScaledClock(clock, scale as double, initialInstant)
            Instant scaledInstant = scaledClock.getScaledInstant(initialInstant.plusMillis(millisecondsToAdd))

        then:
            Duration duration = Duration.between(initialInstant, scaledInstant)
            duration == expectedDuration

        where:
            scale  | millisecondsToAdd | expectedDuration

            0.0001 |                0L | Duration.ZERO
            0.0001 |            10000L | Duration.ofMillis(1)
            0.0001 |            20000L | Duration.ofMillis(2)
            0.0001 |            30000L | Duration.ofMillis(3)
            0.0001 |            40000L | Duration.ofMillis(4)
            0.0001 |            50000L | Duration.ofMillis(5)
            0.0001 |            60000L | Duration.ofMillis(6)
            0.0001 |            70000L | Duration.ofMillis(7)
            0.0001 |            80000L | Duration.ofMillis(8)
            0.0001 |            90000L | Duration.ofMillis(9)
            0.0001 |           100000L | Duration.ofMillis(10)

            0.001  |                0L | Duration.ZERO
            0.001  |             1000L | Duration.ofMillis(1)
            0.001  |             2000L | Duration.ofMillis(2)
            0.001  |             3000L | Duration.ofMillis(3)
            0.001  |             4000L | Duration.ofMillis(4)
            0.001  |             5000L | Duration.ofMillis(5)
            0.001  |             6000L | Duration.ofMillis(6)
            0.001  |             7000L | Duration.ofMillis(7)
            0.001  |             8000L | Duration.ofMillis(8)
            0.001  |             9000L | Duration.ofMillis(9)
            0.001  |            10000L | Duration.ofMillis(10)

            0.01   |                0L | Duration.ZERO
            0.01   |             1000L | Duration.ofMillis(10)
            0.01   |             2000L | Duration.ofMillis(20)
            0.01   |             3000L | Duration.ofMillis(30)
            0.01   |             4000L | Duration.ofMillis(40)
            0.01   |             5000L | Duration.ofMillis(50)
            0.01   |             6000L | Duration.ofMillis(60)
            0.01   |             7000L | Duration.ofMillis(70)
            0.01   |             8000L | Duration.ofMillis(80)
            0.01   |             9000L | Duration.ofMillis(90)
            0.01   |            10000L | Duration.ofMillis(100)

            0.1    |                0L | Duration.ZERO
            0.1    |             1000L | Duration.ofMillis(100)
            0.1    |             2000L | Duration.ofMillis(200)
            0.1    |             3000L | Duration.ofMillis(300)
            0.1    |             4000L | Duration.ofMillis(400)
            0.1    |             5000L | Duration.ofMillis(500)
            0.1    |             6000L | Duration.ofMillis(600)
            0.1    |             7000L | Duration.ofMillis(700)
            0.1    |             8000L | Duration.ofMillis(800)
            0.1    |             9000L | Duration.ofMillis(900)
            0.1    |            10000L | Duration.ofMillis(1000)

            0.5    |                0L | Duration.ZERO
            0.5    |             1000L | Duration.ofMillis(500)
            0.5    |             2000L | Duration.ofMillis(1000)
            0.5    |             3000L | Duration.ofMillis(1500)
            0.5    |             4000L | Duration.ofMillis(2000)
            0.5    |             5000L | Duration.ofMillis(2500)
            0.5    |             6000L | Duration.ofMillis(3000)
            0.5    |             7000L | Duration.ofMillis(3500)
            0.5    |             8000L | Duration.ofMillis(4000)
            0.5    |             9000L | Duration.ofMillis(4500)
            0.5    |            10000L | Duration.ofMillis(5000)

            1.5    |                0L | Duration.ZERO
            1.5    |             1000L | Duration.ofMillis(1500)
            1.5    |             2000L | Duration.ofMillis(3000)
            1.5    |             3000L | Duration.ofMillis(4500)
            1.5    |             4000L | Duration.ofMillis(6000)
            1.5    |             5000L | Duration.ofMillis(7500)
            1.5    |             6000L | Duration.ofMillis(9000)
            1.5    |             7000L | Duration.ofMillis(10500)
            1.5    |             8000L | Duration.ofMillis(12000)
            1.5    |             9000L | Duration.ofMillis(13500)
            1.5    |            10000L | Duration.ofMillis(15000)

            100    |                0L | Duration.ZERO
            100    |             2500L | Duration.ofMillis(250000)
            100    |             5000L | Duration.ofMillis(500000)
            100    |             7500L | Duration.ofMillis(750000)
            100    |            10000L | Duration.ofMillis(1000000)
            100    |            12500L | Duration.ofMillis(1250000)
            100    |            15000L | Duration.ofMillis(1500000)

            1000   |                0L | Duration.ZERO
            1000   |             2500L | Duration.ofMillis(2500000)
            1000   |             5000L | Duration.ofMillis(5000000)
            1000   |             7500L | Duration.ofMillis(7500000)
            1000   |            10000L | Duration.ofMillis(10000000)
            1000   |            12500L | Duration.ofMillis(12500000)
            1000   |            15000L | Duration.ofMillis(15000000)

            10000  |                0L | Duration.ZERO
            10000  |             2500L | Duration.ofMillis(25000000)
            10000  |             5000L | Duration.ofMillis(50000000)
            10000  |             7500L | Duration.ofMillis(75000000)
            10000  |            10000L | Duration.ofMillis(100000000)
            10000  |            12500L | Duration.ofMillis(125000000)
            10000  |            15000L | Duration.ofMillis(150000000)
    }
}
