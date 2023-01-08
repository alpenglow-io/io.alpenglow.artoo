package re.artoo.lance.func;

@FunctionalInterface
public interface TryConsumer4<A, B, C, D> {
  void invoke(A a, B b, C c, D d) throws Throwable;

  default void accept(A a, B b, C c, D d) {
    try {
      invoke(a, b, c, d);
    } catch (Throwable throwable) {
      throw new InvokeException(throwable);
    }
  }
}
