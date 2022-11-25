package io.alpenglow.artoo.lance.func;

import java.util.concurrent.Callable;

@FunctionalInterface
public interface TryRunnable extends Runnable, Callable<Void> {
  void tryRun() throws Throwable;

  @Override
  default void run() {
    try {
      tryRun();
    } catch (Throwable throwable) {
      throw new LambdaCallException(throwable);
    }
  }

  @Override
  default Void call() {
    run();
    return null;
  }
}
