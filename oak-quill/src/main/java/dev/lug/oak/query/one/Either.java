package dev.lug.oak.query.one;

import dev.lug.oak.cursor.Cursor;
import dev.lug.oak.func.Fun;
import dev.lug.oak.query.Queryable;

import static dev.lug.oak.type.Nullability.nonNullable;
import static java.util.Objects.requireNonNullElse;

public interface Either<T> extends Queryable<T> {
  default One<T> or(final T value) {
    return nonNullable(value, it -> () -> Cursor.once(requireNonNullElse(this.iterator().next(), value)));
  }

  default <E extends RuntimeException> One<T> or(final String message, final Fun<? super String, ? extends E> exception) {
    return nonNullable(message, m -> nonNullable(exception, e -> () -> {
      final var value = this.iterator().next();
      if (value == null) throw e.apply(m);
      return Cursor.once(value);
    }));
  }
}
