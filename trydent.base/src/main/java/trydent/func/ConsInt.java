package trydent.func;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntConsumer;

import static java.util.Objects.requireNonNull;

@FunctionalInterface
public interface ConsInt extends IntConsumer, Cons<Integer>, Functional.Con {
  @NotNull
  @Contract(pure = true)
  static ConsInt nothing() {
    return it -> {};
  }

  void acceptInt(int value);

  @Override
  default void accept(int value) { acceptInt(value); }

  @Override
  default void accept(Integer it) { acceptInt(it);}

  default ConsInt then(ConsInt after) {
    requireNonNull(after);
    return t -> { accept(t); after.accept(t); };
  }
}
