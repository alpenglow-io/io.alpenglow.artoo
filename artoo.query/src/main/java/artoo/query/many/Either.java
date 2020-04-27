package artoo.query.many;

import artoo.func.Func;
import artoo.func.Suppl;
import artoo.query.Many;
import artoo.query.Queryable;
import artoo.query.impl.Or;

import static artoo.type.Nullability.nonNullable;

public interface Either<T> extends Queryable<T> {
  @SuppressWarnings("unchecked")
  default Many<T> or(final T... values) {
    nonNullable(values, "values");
    return or(Many.from(values));
  }

  default Many<T> or(final Many<T> many) {
    nonNullable(many, "many");
    return new Or<>(this, many, "Inconsistent queryable.", IllegalStateException::new)::iterator;
  }

  default <E extends RuntimeException> Many<T> or(final String message, final Func<? super String, ? extends E> exception) {
    nonNullable(message, "message");
    nonNullable(exception, "exception");
    return new Or<>(this, Many.none(), message, exception)::iterator;
  }

  default <E extends RuntimeException> Many<T> or(final Suppl<? extends E> exception) {
    nonNullable(exception, "exception");
    return or("nothing", it -> exception.get());
  }
}
