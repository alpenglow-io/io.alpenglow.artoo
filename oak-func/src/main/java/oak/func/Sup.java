package oak.func;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@FunctionalInterface
public interface Sup<T> extends Supplier<T>, Func<Void, T>, Functional.Sup {
  @Contract(pure = true)
  static <T> Sup<T> none() { return () -> null; }

  @Override
  default T apply(final Void none) {
    return get();
  }
}
