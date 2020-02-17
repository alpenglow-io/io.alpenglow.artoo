package dev.lug.oak.type;

import dev.lug.oak.cursor.Cursor;
import dev.lug.oak.func.Con;
import dev.lug.oak.func.Exe;
import dev.lug.oak.func.Pre;
import dev.lug.oak.func.Sup;
import dev.lug.oak.query.one.One;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static java.util.Objects.requireNonNull;

public interface Any<T> extends Iterable<T>, Sup<T> {

  default One<T> filter(final Pre<T> predicate) {
    return One.of(get()).where(predicate);
  }

  default void eventually(final Con<T> then) {
    eventually(then, () -> {});
  }

  default void eventually(final Con<T> then, final Exe eventually) {
    if (this.iterator().hasNext()) {
      for (final var value : this) requireNonNull(then, "Then is null").accept(value);
    } else {
      requireNonNull(eventually, "Eventually is null").run();
    }
  }

  @NotNull
  @Override
  default Iterator<T> iterator() {
    return Cursor.ofNullable(this.get());
  }
}
