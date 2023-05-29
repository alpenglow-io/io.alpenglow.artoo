package re.artoo.lance.func;

@FunctionalInterface
public interface TryConsumer3<A, B, C> extends Invocable {
  void invoke(A a, B b, C c) throws Throwable;

  default void accept(A a, B b, C c) {
    attempt(() -> invoke(a, b, c));
  }
}
