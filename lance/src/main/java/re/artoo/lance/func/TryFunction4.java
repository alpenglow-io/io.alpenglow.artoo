package re.artoo.lance.func;

@FunctionalInterface
public interface TryFunction4<A, B, C, D, R> extends Invocable {
  R invoke(A a, B b, C c, D d) throws Throwable;

  default R apply(A a, B b, C c, D d) {
    return attempt(() -> invoke(a, b, c, d));
  }
}
