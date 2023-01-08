package re.artoo.lance.func;

@FunctionalInterface
public interface TryIntFunction1<T, R> {
  R invoke(int integer, T t) throws Throwable;

  default R apply(int integer, T t) {
    try {
      return invoke(integer, t);
    } catch (Throwable throwable) {
      throw new InvokeException(throwable);
    }
  }

  default <V> TryIntFunction1<V, R> before(TryIntFunction1<? super V, ? extends T> before) {
    // this is actually before, since we invoke the current function after the next function
    return (integer, it) -> invoke(integer, before.invoke(integer, it));
  }

  default <V> TryIntFunction1<T, V> after(TryIntFunction1<? super R, ? extends V> after) {
    // this is actually after, since before we invoke the current function and the next function afterwards
    return (integer, it) -> after.invoke(integer, invoke(integer, it));
  }
}
