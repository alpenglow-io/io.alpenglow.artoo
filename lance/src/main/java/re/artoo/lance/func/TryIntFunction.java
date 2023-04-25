package re.artoo.lance.func;

import java.util.function.IntFunction;

@FunctionalInterface
public interface TryIntFunction<R> extends IntFunction<R>, Invocable {
  R invoke(int value) throws Throwable;

  default R apply(int integer) {
    return attempt(() -> invoke(integer));
  }
}
