package re.artoo.lance.func;

@FunctionalInterface
public interface TryConsumer4<A, B, C, D> extends Invocable {
  void invoke(A a, B b, C c, D d) throws Throwable;

  default void accept(A a, B b, C c, D d) {
    attempt(() -> invoke(a, b, c, d));
  }
}
