package com.yelstream.time.build;

import java.time.Clock;

/**
 *
 * @author Morten Sabroe Mortenen
 * @version 1.0
 * @since 2021-12-19
 */
@FunctionalInterface
public interface ClockParser {
    Clock parse(final ClockDefinition clockDefinition);
}
