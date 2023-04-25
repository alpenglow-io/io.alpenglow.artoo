package re.artoo.lance.func;

@FunctionalInterface
public interface TryIntConsumer1<A> extends Invocable {
  void invoke(int integer, A a) throws Throwable;

  default void accept(int integer, A a) {
    attempt(() -> invoke(integer, a));
  }
}
