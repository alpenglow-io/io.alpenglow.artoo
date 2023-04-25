package re.artoo.lance.func;

@FunctionalInterface
public interface TryFunction6<A, B, C, D, E, F, R> extends Invocable {
  R invoke(A a, B b, C c, D d, E e, F f) throws Throwable;

  default R apply(A a, B b, C c, D d, E e, F f) {
    return attempt(() -> invoke(a, b, c, d, e, f));
  }
}
