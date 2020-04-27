package artoo.query.one.impl;

import artoo.func.Func;
import artoo.func.Suppl;
import artoo.query.One;
import artoo.query.impl.Or;

import static artoo.type.Nullability.nonNullable;

public interface Either<T> extends Sneakable<T> {
  default One<T> or(final T value) {
    nonNullable(value, "value");
    return or(One.just(value));
  }

  default One<T> or(final One<T> one) {
    nonNullable(one, "value");
    return new Or<>(this, this.sneak(), one, "Inconsistent queryable.", it -> new IllegalStateException())::iterator;
  }

  default <E extends RuntimeException> One<T> or(final String message, final Func<? super String, ? extends E> exception) {
    nonNullable(message, "message");
    nonNullable(exception, "exception");
    return new Or<>(this, this.sneak(), One.none(), message, exception)::iterator;
  }

  default <E extends RuntimeException> One<T> or(final Suppl<? extends E> exception) {
    nonNullable(exception, "exception");
    return or("nothing", it -> exception.get());
  }

}
