package oak.func.$2;

import oak.func.Functional;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface IntFunc<T, R> extends Func<Integer, T, R>, Functional.Fun {
  @NotNull
  @Contract(pure = true)
  static <T> IntFunc<T, T> identity() {
    return (index, value) -> value;
  }

  R applyInt(final int param1, final T param2);

  @Override
  default R apply(Integer integer, T t) {
    return applyInt(integer, t);
  }
}
