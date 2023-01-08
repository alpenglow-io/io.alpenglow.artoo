package re.artoo.lance.func;

@FunctionalInterface
public interface TryConsumer5<A, B, C, D, E> {
  void invoke(A a, B b, C c, D d, E e) throws Throwable;

  default void accept(A a, B b, C c, D d, E e) {
    try {
      invoke(a, b, c, d, e);
    } catch (Throwable throwable) {
      throw new InvokeException(throwable);
    }
  }
}
