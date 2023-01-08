package re.artoo.lance.func;

import java.util.concurrent.Callable;

@FunctionalInterface
public interface TryRunnable extends Runnable, Callable<Void> {
  void invoke() throws Throwable;

  @Override
  default void run() {
    try {
      invoke();
    } catch (Throwable throwable) {
      throw new InvokeException(throwable);
    }
  }

  @Override
  default Void call() {
    run();
    return null;
  }
}
