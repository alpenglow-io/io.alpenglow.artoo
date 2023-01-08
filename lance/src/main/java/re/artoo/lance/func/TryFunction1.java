package re.artoo.lance.func;

import java.util.function.Function;

@FunctionalInterface
public interface TryFunction1<T, R> extends Function<T, R> {
  R invoke(T t) throws Throwable;

  @Override
  default R apply(T t) {
    try {
      return invoke(t);
    } catch (Throwable throwable) {
      throw new InvokeException(throwable);
    }
  }

  default <V> TryFunction1<V, R> before(TryFunction1<? super V, ? extends T> before) {
    // this is actually before, since we invoke the current function after the next function
    return it -> invoke(before.invoke(it));
  }

  default <V> TryFunction1<T, V> after(TryFunction1<? super R, ? extends V> after) {
    // this is actually after, since before we invoke the current function and the next function afterwards
    return it -> after.invoke(invoke(it));
  }
}
