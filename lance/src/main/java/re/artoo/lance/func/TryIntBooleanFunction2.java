package re.artoo.lance.func;

@FunctionalInterface
public interface TryIntBooleanFunction2<A, B, R> {
  R invoke(int value, boolean truth, A a, B b) throws Throwable;

  default R apply(int value, boolean truth, A a, B b) {
    try {
      return invoke(value, truth, a, b);
    } catch (Throwable throwable) {
      throw new InvokeException(throwable);
    }
  }
}
