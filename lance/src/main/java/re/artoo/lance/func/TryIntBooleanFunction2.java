package re.artoo.lance.func;

@FunctionalInterface
public interface TryIntBooleanFunction2<A, B, R> extends Invocable {
  R invoke(int value, boolean truth, A a, B b) throws Throwable;

  default R apply(int value, boolean truth, A a, B b) {
    return attempt(() -> invoke(value, truth, a, b));
  }
}
