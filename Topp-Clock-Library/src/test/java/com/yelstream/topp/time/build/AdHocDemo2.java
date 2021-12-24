package com.yelstream.topp.time.build;

import com.yelstream.topp.time.Clocks;
import com.yelstream.topp.time.declare.ClockDeclaration;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * <p>
 * Ad-hoc demonstration.
 * </p>
 * <p>
 * This is to be run manually.
 * </p>
 * @author Morten Sabroe Mortenen
 * @version 1.0
 * @since 2021-12-21
 */
public class AdHocDemo2 {
    public static void main(final String[] args) throws Exception {
        Clock referenceClock = Clocks.createClockSystemInDefaultZone();

        Clock backwardClock = ClockDeclaration.createClock("systemInDefaultZone(negate)");
        Clock fastestClock = ClockDeclaration.createClock("systemInDefaultZone(multiplyBy=100)");
        Clock fasterClock = ClockDeclaration.createClock("systemInDefaultZone(multiplyBy=2)");
        Clock fastClock = ClockDeclaration.createClock("systemInDefaultZone(multiplyBy=3,divideBy=2)");
        Clock slowerClock = ClockDeclaration.createClock("systemInDefaultZone(divideBy=2)");

        while (true) {
            System.out.println();
            System.out.println("Backward:  "+LocalDateTime.now(backwardClock));
            System.out.println("Slower:    "+LocalDateTime.now(slowerClock));
            System.out.println("Reference: "+LocalDateTime.now(referenceClock));
            System.out.println("Fast:      "+LocalDateTime.now(fastClock));
            System.out.println("Faster:    "+LocalDateTime.now(fasterClock));
            System.out.println("Fastest:   "+LocalDateTime.now(fastestClock));
            Thread.sleep(2000L);
        }
    }
}
