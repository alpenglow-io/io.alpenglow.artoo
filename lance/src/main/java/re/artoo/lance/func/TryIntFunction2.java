package re.artoo.lance.func;

@FunctionalInterface
public interface TryIntFunction2<A, B, R> extends Invocable {
  R invoke(int integer, A a, B b) throws Throwable;

  default R apply(int integer, A a, B b) {
    return attempt(() -> invoke(integer, a, b));
  }
}
