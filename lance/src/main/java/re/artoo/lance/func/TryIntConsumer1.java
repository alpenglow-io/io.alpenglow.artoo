package re.artoo.lance.func;

import java.util.function.Consumer;

@FunctionalInterface
public interface TryIntConsumer1<A> {
  void invoke(int integer, A a) throws Throwable;

  default void accept(int integer, A a) {
    try {
      invoke(integer, a);
    } catch (Throwable throwable) {
      throw new InvokeException(throwable);
    }
  }
}
