package re.artoo.lance.func;

@FunctionalInterface
public interface TryFunction7<A, B, C, D, E, F, G, R> extends Invocable {
  R invoke(A a, B b, C c, D d, E e, F f, G g) throws Throwable;

  default R apply(A a, B b, C c, D d, E e, F f, G g) {
    return attempt(() -> invoke(a, b, c, d, e, f, g));
  }
}
