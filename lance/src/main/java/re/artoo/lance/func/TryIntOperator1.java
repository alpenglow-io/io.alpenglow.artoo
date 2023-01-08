package re.artoo.lance.func;

import java.util.function.UnaryOperator;

@FunctionalInterface
public interface TryIntOperator1<T> extends UnaryOperator<T> {
  T invoke(T t) throws Throwable;
  @Override
  default T apply(T t) {
    try {
      return invoke(t);
    } catch (Throwable throwable) {
      throw new InvokeException(throwable);
    }
  }
}
