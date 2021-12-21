package com.yelstream.topp.time;

import lombok.RequiredArgsConstructor;

import java.time.*;

/**
 * <p>
 * Clock running at a speed scaled relative to a reference clock.
 * </p>
 * <p>
 * This finds scaled differences by direct computation upon instances of {@link Instant}.
 * </p>
 * @author Morten Sabroe Mortenen
 * @version 1.0
 * @since 2021-12-21
 */
@RequiredArgsConstructor
class InstantScaledClock extends Clock {

    private final Clock clock;
    private final double scale;
    private final long numeratorScale;
    private final long denominatorScale;
    private final Instant instant;

    private static final long nanosecondsPerSecond = 1_000_000_000L;
    private static final long defaultDenominatorScale = 10L*10L*24L*60L*60L;  //10*10*24*60*60 = 2^8 x 3^3 x 5^3

    public InstantScaledClock(Clock clock,
                              double scale,
                              Instant instant) {
        this(clock, scale, (long)(defaultDenominatorScale*scale), defaultDenominatorScale, instant);
    }

    public InstantScaledClock(Clock clock,
                              double scale) {
        this(clock, scale, clock.instant());
    }

    @Override
    public ZoneId getZone() {
        return clock.getZone();
    }

    @Override
    public Clock withZone(ZoneId zone) {
        if (zone.equals(this.getZone())) {
            return this;
        }
        return new InstantScaledClock(clock.withZone(zone), scale, instant);
    }

    private static Instant getScaledInstant(Instant instant0,
                                            Instant instant1,
                                            long numeratorScale,
                                            long denominatorScale) {
        long epochSecond0 = instant0.getEpochSecond();
        int nanoAdjustment0 = instant0.getNano();

        long epochSecond1 = instant1.getEpochSecond();
        int nanoAdjustment1 = instant1.getNano();

        long deltaEpochSecond = epochSecond1 - epochSecond0;
        long deltaNanoAdjustment = nanoAdjustment1 - nanoAdjustment0;

        if (deltaNanoAdjustment<0) {
            deltaEpochSecond--;
            deltaNanoAdjustment+=nanosecondsPerSecond;
        }

        long epochSecond = epochSecond0 + (deltaEpochSecond*numeratorScale)/denominatorScale;
//        long nanoAdjustment = nanoAdjustment0 + (((deltaEpochSecond*numeratorScale)%denominatorScale)*nanosecondsPerSecond)/denominatorScale + (deltaNanoAdjustment*numeratorScale+denominatorScale/2)/denominatorScale;
        long _nanoAdjustment = nanoAdjustment0 + (((deltaEpochSecond*numeratorScale)%denominatorScale)*nanosecondsPerSecond)/denominatorScale + (deltaNanoAdjustment*numeratorScale+denominatorScale/2)/denominatorScale;
        long nanoAdjustment = nanoAdjustment0 + (((deltaEpochSecond*numeratorScale)%denominatorScale)*nanosecondsPerSecond + deltaNanoAdjustment*numeratorScale+denominatorScale/2)/denominatorScale;
        System.out.println("_nanoAdjustment: "+_nanoAdjustment);
        System.out.println("nanoAdjustment: "+nanoAdjustment);
/*
        epochSecond += nanoAdjustment/nanosecondsPerSecond;
        nanoAdjustment = nanoAdjustment%nanosecondsPerSecond;
*/

        return Instant.ofEpochSecond(epochSecond, nanoAdjustment);
    }

    public Instant getScaledInstant(Instant instant) {
        return getScaledInstant(this.instant, instant, numeratorScale, denominatorScale);
    }

    @Override
    public Instant instant() {
        return getScaledInstant(clock.instant());
    }
}
