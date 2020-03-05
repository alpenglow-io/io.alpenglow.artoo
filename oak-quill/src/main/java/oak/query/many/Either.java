package oak.query.many;

import oak.func.Func;
import oak.func.Suppl;
import oak.query.Many;
import oak.query.Queryable;
import oak.query.internal.Or;

import static oak.type.Nullability.nonNullable;

public interface Either<T> extends Queryable<T> {
  @SuppressWarnings("unchecked")
  default Many<T> or(final T... values) {
    nonNullable(values, "values");
    return new Or<>(this, Many.from(values))::iterator;
  }

  default Many<T> or(final Many<T> many) {
    nonNullable(many, "many");
    return new Or<>(this, many)::iterator;
  }

  default <E extends RuntimeException> Many<T> or(final String message, final Func<? super String, ? extends E> exception) {
    nonNullable(message, "message");
    nonNullable(exception, "exception");
    return new Or<>(this, message, exception)::iterator;
  }

  default <E extends RuntimeException> Many<T> or(final Suppl<? extends E> exception) {
    nonNullable(exception, "exception");
    return new Or<>(this, exception)::iterator;
  }
}
