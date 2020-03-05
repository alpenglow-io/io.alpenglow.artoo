package oak.type;

import oak.cursor.Cursor;
import oak.func.Cons;
import oak.func.Exec;
import oak.func.Pred;
import oak.func.Suppl;
import oak.query.One;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static java.util.Objects.requireNonNull;

public interface Any<T> extends Iterable<T>, Suppl<T> {

  default One<T> filter(final Pred<T> predicate) {
    return One.of(get()).where(predicate);
  }

  default void eventually(final Cons<T> then) {
    eventually(then, () -> {});
  }

  default void eventually(final Cons<T> then, final Exec eventually) {
    if (this.iterator().hasNext()) {
      for (final var value : this) requireNonNull(then, "Then is null").accept(value);
    } else {
      requireNonNull(eventually, "Eventually is null").run();
    }
  }

  @NotNull
  @Override
  default Iterator<T> iterator() {
    return Cursor.of(this.get());
  }
}
