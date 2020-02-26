package oak.func;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

@FunctionalInterface
public interface Cons<T> extends Consumer<T>, Functional.Con {
  @NotNull
  @Contract(pure = true)
  static <T> Cons<T> nothing() {
    return it -> {};
  }

  default Cons<T> then(Cons<? super T> after) {
    requireNonNull(after);
    return t -> { accept(t); after.accept(t); };
  }
}
