package re.artoo.lance.func;

@FunctionalInterface
public interface TryFunction7<A, B, C, D, E, F, G, R> {
  R invoke(A a, B b, C c, D d, E e, F f, G g) throws Throwable;

  default R apply(A a, B b, C c, D d, E e, F f, G g) {
    try {
      return invoke(a, b, c, d, e, f, g);
    } catch (Throwable throwable) {
      throw new InvokeException(throwable);
    }
  }
}
