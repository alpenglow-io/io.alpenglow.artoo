package oak.collect.query;

import oak.func.con.Consumer1;
import oak.func.exe.Executable;

import static java.util.Objects.requireNonNull;

public interface Functor<T, M extends Iterable<T>> extends Iterable<T> {
  default void eventually(final Consumer1<T> then) {
    eventually(then, () -> {});
  }

  default void eventually(final Consumer1<T> then, final Executable eventually) {
    if (this.iterator().hasNext()) {
      for (final var value : this) requireNonNull(then, "Then is null").accept(value);
    } else {
      requireNonNull(eventually, "Eventually is null").run();
    }
  }
}
