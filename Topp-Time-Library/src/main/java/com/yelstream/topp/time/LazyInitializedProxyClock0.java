package com.yelstream.topp.time;

import com.yelstream.topp.util.InstanceProvider;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
@EqualsAndHashCode(callSuper=true)
@ToString
public class LazyInitializedProxyClock0 extends AbstractProxyClock {
    private InstanceProvider<Clock> instanceProvider;

    /**
     * Constructor.
     * @param clockFactory Factory for creating the wrapped clock instance.
     */
    public LazyInitializedProxyClock0(Supplier<Clock> clockFactory) {
        this.instanceProvider=new InstanceProvider<>(clockFactory);
    }

    /**
     * Sets the factory for creating the wrapped clock instance.
     * Beware that this resets the clock.
     * @param clockFactory Factory for creating the wrapped clock instance.
     */
    public void setClockFactory(Supplier<Clock> clockFactory) {
        this.instanceProvider=new InstanceProvider<>(clockFactory);
    }

    @Override
    protected final Clock getClock() {
        return instanceProvider.get();
    }
}
