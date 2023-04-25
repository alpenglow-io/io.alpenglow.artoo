package re.artoo.lance.func;

@FunctionalInterface
public interface TryFunction3<A, B, C, R> extends Invocable {
  R invoke(A a, B b, C c) throws Throwable;

  default R apply(A a, B b, C c) {
    return attempt(() -> invoke(a, b, c));
  }
}
