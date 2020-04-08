package trydent.query.many;

import trydent.func.Func;
import trydent.func.Suppl;
import trydent.query.Many;
import trydent.query.Queryable;
import trydent.query.internal.Or;

import static trydent.type.Nullability.nonNullable;

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
