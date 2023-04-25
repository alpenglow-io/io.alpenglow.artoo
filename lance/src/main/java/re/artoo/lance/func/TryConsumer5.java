package re.artoo.lance.func;

@FunctionalInterface
public interface TryConsumer5<A, B, C, D, E> extends Invocable {
  void invoke(A a, B b, C c, D d, E e) throws Throwable;

  default void accept(A a, B b, C c, D d, E e) {
    attempt(() -> invoke(a, b, c, d, e));
  }
}
