package com.yelstream.topp.time.build;

import com.yelstream.topp.time.declare.ClockDeclaration;
import com.yelstream.topp.time.declare.ClockDeclarations;
import lombok.extern.slf4j.Slf4j;

import java.time.Clock;

@Deprecated
@Slf4j
public final class SystemClock {
    private static class InstanceHolder {
        private static final SystemClock INSTANCE = createInstance();

        private static SystemClock createInstance() {
            Clock clock = createClock();
            return new SystemClock(clock);
        }

        private static Clock createClock() {
            Clock clock = null;
            ClockDeclaration clockDeclaration = ClockDeclarations.getClockDefinitionFromEnvironment(null);
            if (clockDeclaration != null) {
                clock = clockDeclaration.toClock();
                log.info("System clock set; using settings read from environment, clock definition is {}.", clockDeclaration);
            } else {
                clock = Clock.systemDefaultZone();
                log.info("System clock set; using default settings.");
            }
            return clock;
        }
    }

    public static SystemClock getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public static Clock getSystemClock() {
        return getInstance().clock;
    }

    private SystemClock(final Clock clock) {
        this.clock = clock;
    }

    private final Clock clock;

    public Clock getClock() {
        return clock;
    }
}
