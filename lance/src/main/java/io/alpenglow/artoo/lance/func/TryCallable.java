package io.alpenglow.artoo.lance.func;

import java.util.concurrent.Callable;

@FunctionalInterface
public interface TryCallable<T> extends Callable<T> {
  T tryCall() throws Throwable;
  @Override
  default T call() {
    try {
      return tryCall();
    } catch (Throwable throwable) {
      throw new LambdaCallException(throwable);
    }
  }
}
