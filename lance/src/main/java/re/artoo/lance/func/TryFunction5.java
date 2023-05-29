package re.artoo.lance.func;

@FunctionalInterface
public interface TryFunction5<A, B, C, D, E, R> extends Invocable {
  R invoke(A a, B b, C c, D d, E e) throws Throwable;

  default R apply(A a, B b, C c, D d, E e) {
    return attempt(() -> invoke(a, b, c, d, e));
  }
}
