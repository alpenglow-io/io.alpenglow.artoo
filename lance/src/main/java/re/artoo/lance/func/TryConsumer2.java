package re.artoo.lance.func;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface TryConsumer2<A, B> extends BiConsumer<A, B> {
  void invoke(A a, B b) throws Throwable;
  @Override
  default void accept(A a, B b) {
    try {
      invoke(a, b);
    } catch (Throwable throwable) {
      throw new LambdaCallException(throwable);
    }
  }
}
