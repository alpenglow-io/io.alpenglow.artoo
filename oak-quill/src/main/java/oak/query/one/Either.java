package oak.query.one;

import oak.cursor.Cursor;
import oak.func.Func;
import oak.query.Queryable;
import oak.type.Nullability;

import static oak.type.Nullability.nonNullable;
import static java.util.Objects.requireNonNullElse;

public interface Either<T> extends Queryable<T> {
  default One<T> or(final T value) {
    return Nullability.nonNullable(value, it -> () -> Cursor.once(requireNonNullElse(this.iterator().next(), value)));
  }

  default <E extends RuntimeException> One<T> or(final String message, final Func<? super String, ? extends E> exception) {
    return Nullability.nonNullable(message, m -> Nullability.nonNullable(exception, e -> () -> {
      final var value = this.iterator().next();
      if (value == null) throw e.apply(m);
      return Cursor.once(value);
    }));
  }
}
