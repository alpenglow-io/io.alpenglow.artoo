package artoo.func;

import org.jetbrains.annotations.Contract;

import java.util.function.Supplier;

@FunctionalInterface
public interface Suppl<T> extends Supplier<T>, Func<Void, T>, Functional.Sup {
  @Contract(pure = true)
  static <T> Suppl<T> none() { return () -> null; }

  @Override
  default T apply(final Void none) {
    return get();
  }
}
