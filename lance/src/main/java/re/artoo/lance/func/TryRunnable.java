package re.artoo.lance.func;

import java.util.concurrent.Callable;

@FunctionalInterface
public interface TryRunnable extends Runnable, Callable<Void>, Invocable {
  void invoke() throws Throwable;

  @Override
  default void run() {
    attempt(this::invoke);
  }

  @Override
  default Void call() {
    run();
    return null;
  }
}
