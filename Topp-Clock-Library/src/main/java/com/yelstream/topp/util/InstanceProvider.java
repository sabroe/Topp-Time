package com.yelstream.topp.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.function.Supplier;

/**
 * <p>
 *   Provider of a single object instance.
 *   The object is created on-demand and may be lazy initialized.
 *   The instance kept may be queried multiple times through {@link #get()}.
 * </p>
 * <p>
 *   This is thread-safe. Internally, double-checked locking is applied.
 * </p>
 * @param <T> Type of instance.
 * @author Morten Sabroe Mortenen
 * @version 1.0
 * @since 2021-12-25
 */
@NoArgsConstructor
public final class InstanceProvider<T> implements Supplier<T> {
    private Supplier<T> instanceFactory;
    private Holder<T> instanceHolder;

    @AllArgsConstructor
    @Getter
    private static class Holder<X> {
        private final X instance;
    }

    /**
     * Constructor.
     * @param instanceFactory Factory of thje provided object instance.
     */
    public InstanceProvider(Supplier<T> instanceFactory) {
        this.instanceFactory = instanceFactory;
    }

    @Override
    public T get() {
        Holder<T> holder = this.instanceHolder;
        if (holder == null) {
            synchronized (this) {
                holder = this.instanceHolder;
                if (holder == null) {
                    holder = new Holder<>(instanceFactory.get());
                    this.instanceHolder = holder;
                }
            }
        }
        return holder.getInstance();
    }
}
