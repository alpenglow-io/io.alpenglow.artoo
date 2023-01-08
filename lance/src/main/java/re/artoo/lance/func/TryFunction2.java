package re.artoo.lance.func;

import java.util.function.BiFunction;

@FunctionalInterface
public interface TryFunction2<A, B, R> extends BiFunction<A, B, R> {
  R invoke(A a, B b) throws Throwable;

  @Override
  default R apply(A a, B b) {
    try {
      return invoke(a, b);
    } catch (Throwable throwable) {
      throw new InvokeException(throwable);
    }
  }
}
