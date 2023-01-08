package re.artoo.lance.func;

@FunctionalInterface
public interface TryIntFunction2<A, B, R> {
  R invoke(int integer, A a, B b) throws Throwable;

  default R apply(int integer, A a, B b) {
    try {
      return invoke(integer, a, b);
    } catch (Throwable throwable) {
      throw new InvokeException(throwable);
    }
  }
}
