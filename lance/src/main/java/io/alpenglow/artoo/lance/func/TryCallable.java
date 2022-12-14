package io.alpenglow.artoo.lance.func;

import java.util.concurrent.Callable;

@FunctionalInterface
public interface TryCallable<T> extends Callable<T> {
  T invoke() throws Throwable;
  @Override
  default T call() {
    try {
      return invoke();
    } catch (Throwable throwable) {
      throw new LambdaCallException(throwable);
    }
  }
}
