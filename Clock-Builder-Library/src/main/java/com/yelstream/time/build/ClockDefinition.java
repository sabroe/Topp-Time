package com.yelstream.time.build;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.Clock;

/**
 * Definition of a {@link java.time.Clock} instance.
 */
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class ClockDefinition {
    @Getter
    private final String declaration;

//    public

    public Clock toClock() {
        return ClockBuilder.parse(declaration).build();
    }
}
