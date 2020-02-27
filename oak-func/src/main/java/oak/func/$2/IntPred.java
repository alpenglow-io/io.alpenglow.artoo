package oak.func.$2;

import oak.func.Functional;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface IntPred<T> extends Pred<Integer, T>, Func<Integer, T, Boolean>, Functional.Pre {
  @NotNull
  @Contract(pure = true)
  static <T> IntPred<T> tautology() {
    return (index, value) -> true;
  }

  boolean verify(final int index, final T param);

  @Override
  default Boolean apply(Integer index, T param) {
    return verify(index, param);
  }

  @Override
  default boolean test(Integer index, T param) {
    return verify(index, param);
  }
}
