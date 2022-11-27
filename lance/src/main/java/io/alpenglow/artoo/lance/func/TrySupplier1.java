package io.alpenglow.artoo.lance.func;

import java.util.function.Supplier;

@FunctionalInterface
public interface TrySupplier1<A> extends Supplier<A> {
  A tryGet() throws Throwable;
  @Override
  default A get() {
    try {
      return tryGet();
    } catch (Throwable throwable) {
      throw new LambdaCallException(throwable);
    }
  }
}
