package com.yelstream.topp.time;

import lombok.NoArgsConstructor;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

/**
 * <p>
 *   Abstract proxy for {@link Clock} instances.
 * </p>
 * @author Morten Sabroe Mortenen
 * @version 1.0
 * @since 2021-12-25
 */
@NoArgsConstructor
public abstract class AbstractProxyClock extends Clock {
    /**
     * Gets the wrapped instance.
     * @return Wrapped instance.
     */
    protected abstract Clock getClock();

    @Override
    public ZoneId getZone() {
        return getClock().getZone();
    }

    @Override
    public Clock withZone(ZoneId zone) {
        return getClock().withZone(zone);
    }

    @Override
    public Instant instant() {
        return getClock().instant();
    }
}
