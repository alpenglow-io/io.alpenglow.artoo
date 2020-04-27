package artoo.query.one.internal;

import artoo.func.Func;
import artoo.func.Suppl;
import artoo.query.internal.Or;
import artoo.query.One;

import static artoo.type.Nullability.nonNullable;

public interface Either<T> extends Sneakable<T> {
  default One<T> or(final T value) {
    nonNullable(value, "value");
    return new Or<>(this, this.sneak(), new Just<>(value))::iterator;
  }

  default One<T> or(final One<T> one) {
    nonNullable(one, "value");
    return new Or<>(this, this.sneak(), one)::iterator;
  }

  default <E extends RuntimeException> One<T> or(final String message, final Func<? super String, ? extends E> exception) {
    nonNullable(message, "message");
    nonNullable(exception, "exception");
    return new Or<>(this, this.sneak(), message, exception)::iterator;
  }

  default <E extends RuntimeException> One<T> or(final Suppl<? extends E> exception) {
    nonNullable(exception, "exception");
    return new Or<>(this, this.sneak(), exception)::iterator;
  }

}
