package oak.query.one;

import oak.func.Func;
import oak.func.Suppl;
import oak.query.Queryable;
import oak.query.internal.Or;
import oak.query.One;
import oak.query.one.internal.Just;

import static oak.type.Nullability.nonNullable;

public interface Either<T> extends Queryable<T> {
  default One<T> or(final T value) {
    nonNullable(value, "value");
    return new Or<>(this, new Just<>(value))::iterator;
  }

  default One<T> or(final One<T> one) {
    nonNullable(one, "value");
    return new Or<>(this, one)::iterator;
  }

  default <E extends RuntimeException> One<T> or(final String message, final Func<? super String, ? extends E> exception) {
    nonNullable(message, "message");
    nonNullable(exception, "exception");
    return new Or<>(this, message, exception)::iterator;
  }

  default <E extends RuntimeException> One<T> or(final Suppl<? extends E> exception) {
    nonNullable(exception, "exception");
    return new Or<>(this, exception)::iterator;
  }
}

