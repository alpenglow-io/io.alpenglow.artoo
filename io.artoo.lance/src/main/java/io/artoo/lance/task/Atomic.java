package io.artoo.lance.task;

import io.artoo.lance.func.Func;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public interface Atomic<T> {
  <R> Optional<R> let(Func.MaybeFunction<? super T, ? extends R> map, Func.MaybeFunction<? super R, ? extends T> update);

  static <T> Atomic<T> reference(final T value) {
    return new Reference<>(value);
  }

  final class Reference<T> implements Atomic<T> {
    private final AtomicReference<T> reference;

    Reference(final T identity) { this(new AtomicReference<>(identity)); }
    private Reference(final AtomicReference<T> reference) {this.reference = reference;}

    @Override
    public <R> Optional<R> let(final Func.MaybeFunction<? super T, ? extends R> map, final Func.MaybeFunction<? super R, ? extends T> update) {
      T prev = reference.get(), next = null;
      R applied = null;
      for (var haveNext = false;;) {
        if (!haveNext) {
          applied = map.apply(prev);
          next = update.apply(applied);
        }
        if (reference.weakCompareAndSetVolatile(prev, next))
          return Optional.ofNullable(applied);
        haveNext = (prev == (prev = reference.get()));
      }
    }

    @Override
    public int hashCode() {
      return reference.hashCode();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(final Object obj) {
      return this.getClass().isInstance(obj) && reference.equals(((Reference<T>) obj).reference);
    }

    @Override
    public String toString() {
      return "Atomic.Reference { %s }".formatted(reference.toString());
    }
  }
}
