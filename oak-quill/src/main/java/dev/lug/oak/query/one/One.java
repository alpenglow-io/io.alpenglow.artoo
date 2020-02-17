package dev.lug.oak.query.one;

import dev.lug.oak.cursor.Cursor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static dev.lug.oak.type.Nullability.nonNullable;
import static java.util.Objects.nonNull;

public interface One<T> extends Projectable<T>, Filterable<T>, Either<T>, Hookable<T>, Unwrappable<T> {
  static <L> One<L> of(final L value) {
    return nonNull(value) ? One.just(value) : One.none();
  }

  @NotNull
  @Contract("_ -> new")
  static <L> One<L> just(final L value) { return nonNullable(value, v -> () -> Cursor.once(v)); }

  @NotNull
  @Contract(value = " -> new", pure = true)
  static <L> One<L> none() {
    return Cursor::none;
  }
}
