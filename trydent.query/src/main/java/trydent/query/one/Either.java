package trydent.query.one;

import trydent.func.Func;
import trydent.func.Suppl;
import trydent.query.Queryable;
import trydent.query.internal.Or;
import trydent.query.One;
import trydent.query.one.internal.Just;

import static trydent.type.Nullability.nonNullable;

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

