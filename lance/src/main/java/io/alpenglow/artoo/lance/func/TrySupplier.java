package io.alpenglow.artoo.lance.func;

import io.alpenglow.artoo.lance.scope.Maybe;

import java.util.function.Supplier;

@FunctionalInterface
public interface TrySupplier<A> extends Supplier<A> {
  A tryGet() throws Throwable;

  default Maybe<A> maybeGet() {
    try {
      return Maybe.value(tryGet());
    } catch (Throwable throwable) {
      return Maybe.error(throwable);
    }
  }

  @Override
  default A get() {
    return maybeGet().nullable();
  }
}
