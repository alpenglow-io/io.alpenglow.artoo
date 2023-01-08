package re.artoo.lance.func;

@FunctionalInterface
public interface TryFunction4<A, B, C, D, R> {
  R invoke(A a, B b, C c, D d) throws Throwable;

  default R apply(A a, B b, C c, D d) {
    try {
      return invoke(a, b, c, d);
    } catch (Throwable throwable) {
      throw new InvokeException(throwable);
    }
  }
}
