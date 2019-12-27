package dev.lug.oak.func.sup;

import dev.lug.oak.func.Functional;
import dev.lug.oak.func.fun.Function1;

import java.util.function.Supplier;

@FunctionalInterface
public interface Supplier1<T> extends Supplier<T>, Function1<Void, T>, Functional.Sup {
  static <T> Supplier1<T> none() { return () -> null; }

  @Override
  default T apply(final Void none) {
    return get();
  }

  T tryGet() throws Throwable;

  default T get() {
    try {
      return tryGet();
    } catch (final Throwable throwable) {
      throwable.printStackTrace();
      return null;
    }
  }
}
