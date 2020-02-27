package oak.func.$2;

import oak.func.Functional;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface IntCons<T> extends IntFunc<T, Void>, Functional.Fun {
  @NotNull
  @Contract(pure = true)
  static <T> IntCons<T> nothing() {
    return (index, value) -> {};
  }

  void acceptInt(final int param1, final T param2);

  default Void applyInt(final int param1, final T param2) {
    acceptInt(param1, param2);
    return null;
  }

  @Override
  default Void apply(Integer integer, T t) {
    return applyInt(integer, t);
  }
}
