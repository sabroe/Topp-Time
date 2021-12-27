package com.yelstream.topp.time;

import lombok.*;

import java.time.Clock;
import java.util.function.Supplier;

/**
 * <p>
 *   Lazy initialized proxy for {@link Clock} instances.
 * </p>
 * @author Morten Sabroe Mortenen
 * @version 1.0
 * @since 2021-12-25
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=true)
@ToString
public class LazyInitializedProxyClock extends AbstractProxyClock {
    @Getter
    @Setter
    private Supplier<Clock> clockFactory;

    @Getter(lazy=true)
    private final Clock clock=clockFactory.get();
}
