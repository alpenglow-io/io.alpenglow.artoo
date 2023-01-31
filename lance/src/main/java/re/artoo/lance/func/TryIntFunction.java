package re.artoo.lance.func;

import java.util.function.IntFunction;

@FunctionalInterface
public interface TryIntFunction<R> extends IntFunction<R> {
  R invoke(int value) throws Throwable;

  default R apply(int integer) {
    try {
      return invoke(integer);
    } catch (Throwable throwable) {
      throw new InvokeException(throwable);
    }
  }
}
