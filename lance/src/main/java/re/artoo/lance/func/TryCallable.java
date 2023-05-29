package re.artoo.lance.func;

import java.util.concurrent.Callable;

@FunctionalInterface
public interface TryCallable<T> extends Callable<T>, Invocable {
  T invoke() throws Throwable;

  @Override
  default T call() {
    return attempt(this::invoke);
  }
}
