package com.yelstream.time.build;

import lombok.extern.slf4j.Slf4j;

import java.time.Clock;

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
            ClockDefinition clockDefinition = ClockDefinitions.getClockDefinitionFromEnvironment(null);
            if (clockDefinition != null) {
                clock = clockDefinition.toClock();
                log.info("System clock set; using settings read from environment, clock definition is {}.", clockDefinition);
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
