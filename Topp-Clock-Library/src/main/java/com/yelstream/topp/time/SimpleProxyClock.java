package com.yelstream.topp.time;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Clock;

/**
 * <p>
 *   Simple proxy for {@link Clock} instances.
 * </p>
 * @author Morten Sabroe Mortenen
 * @version 1.0
 * @since 2021-12-25
 */
@NoArgsConstructor
@AllArgsConstructor
public class SimpleProxyClock extends AbstractProxyClock {
    @Getter
    @Setter
    private Clock clock;
}
