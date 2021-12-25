package com.yelstream.topp.time.build;

import com.yelstream.topp.time.Clocks;
import com.yelstream.topp.time.declare.ClockDeclaration;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

        while (true) {
            System.out.println();
            System.out.println("Backward:  "+formatter.format(LocalDateTime.now(backwardClock)));
            System.out.println("Slower:    "+formatter.format(LocalDateTime.now(slowerClock)));
            System.out.println("Reference: "+formatter.format(LocalDateTime.now(referenceClock)));
            System.out.println("Fast:      "+formatter.format(LocalDateTime.now(fastClock)));
            System.out.println("Faster:    "+formatter.format(LocalDateTime.now(fasterClock)));
            System.out.println("Fastest:   "+formatter.format(LocalDateTime.now(fastestClock)));
            Thread.sleep(2000L);
        }
    }
}
