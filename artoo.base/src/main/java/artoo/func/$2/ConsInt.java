package artoo.func.$2;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import artoo.func.Functional;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface ConsInt<T> extends BiConsumer<Integer, T>, Cons<Integer, T>, FuncInt<T, Void>, Functional.Fun {
  @NotNull
  @Contract(pure = true)
  static <T> ConsInt<T> nothing() {
    return (index, value) -> {};
  }

  void acceptInt(final int param1, final T param2);

  @Override
  default Void applyInt(final int param1, final T param2) {
    acceptInt(param1, param2);
    return null;
  }

  @Override
  default void accept(Integer integer, T it) {
    acceptInt(integer, it);
  }

  @Override
  default Void apply(Integer integer, T t) {
    return applyInt(integer, t);
  }
}
