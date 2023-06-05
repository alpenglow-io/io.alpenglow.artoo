package re.artoo.lance.func;

@FunctionalInterface
public interface TryIntConsumer2<A, B> extends Invocable {
  void invoke(int integer, A a, B b) throws Throwable;

  default void accept(int integer, A a, B b) {
    attempt(() -> invoke(integer, a, b));
  }
}
