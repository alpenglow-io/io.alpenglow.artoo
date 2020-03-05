package oak.func;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.IntConsumer;

import static java.util.Objects.requireNonNull;

@FunctionalInterface
public interface IntCons extends IntConsumer, Func<Integer, Void>, Functional.Con {
  @NotNull
  @Contract(pure = true)
  static IntCons nothing() {
    return it -> {};
  }

  void acceptInt(int value);

  @Override
  default void accept(int value) { acceptInt(value); }

  @Override
  default Void apply(Integer integer) {
    acceptInt(integer);
    return null;
  }

  default IntCons then(IntCons after) {
    requireNonNull(after);
    return t -> { accept(t); after.accept(t); };
  }
}
