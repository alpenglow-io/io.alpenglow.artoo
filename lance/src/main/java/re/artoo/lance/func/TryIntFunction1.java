package re.artoo.lance.func;

@FunctionalInterface
public interface TryIntFunction1<T, R> extends Invocable {
  R invoke(int integer, T t) throws Throwable;

  default R apply(int integer, T t) {
    return attempt(() -> invoke(integer, t));
  }

  default <V> TryIntFunction1<V, R> invokeAfter(TryIntFunction1<? super V, ? extends T> func) {
    // invoke TryIntFunction1.this after TryIntFunction1.func
    return (integer, it) -> invoke(integer, func.invoke(integer, it));
  }

  default <V> TryIntFunction1<T, V> then(TryIntFunction1<? super R, ? extends V> after) {
    // this is actually after, since before we invoke the current function and the next function afterwards
    return (integer, it) -> after.invoke(integer, invoke(integer, it));
  }
}
