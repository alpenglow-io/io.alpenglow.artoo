package re.artoo.lance.func;

import java.util.function.UnaryOperator;

@FunctionalInterface
public interface TryUnaryOperator<T> extends UnaryOperator<T>, Invocable {
  T invoke(T t) throws Throwable;

  @Override
  default T apply(T t) {
    return attempt(() -> invoke(t));
  }
}
