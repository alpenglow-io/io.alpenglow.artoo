package re.artoo.lance.func;

@FunctionalInterface
public interface TryFunction6<A, B, C, D, E, F, R> {
  R invoke(A a, B b, C c, D d, E e, F f) throws Throwable;

  default R apply(A a, B b, C c, D d, E e, F f) {
    try {
      return invoke(a, b, c, d, e, f);
    } catch (Throwable throwable) {
      throw new LambdaCallException(throwable);
    }
  }
}
