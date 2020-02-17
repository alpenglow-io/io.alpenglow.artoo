package dev.lug.oak.func;

import java.util.function.Supplier;

@FunctionalInterface
public interface Sup<T> extends Supplier<T>, Fun<Void, T>, Functional.Sup {
  static <T> Sup<T> none() { return () -> null; }

  @Override
  default T apply(final Void none) {
    return get();
  }
}
