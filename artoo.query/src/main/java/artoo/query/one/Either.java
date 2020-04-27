package artoo.query.one;

import artoo.func.Func;
import artoo.func.Suppl;
import artoo.query.One;
import artoo.query.Queryable;
import artoo.query.impl.Or;

import static artoo.type.Nullability.nonNullable;

public interface Either<T> extends Queryable<T> {
  default One<T> or(final T value) {
    return or(One.just(nonNullable(value, "value")));
  }

  default <O extends One<T>> One<T> or(final O otherwise) {
    nonNullable(otherwise, "otherwise");
    return new Or<>(this, otherwise, "Inconsistent queryable.", IllegalStateException::new)::iterator;
  }

  default <E extends RuntimeException> One<T> or(final String message, final Func<? super String, ? extends E> exception) {
    nonNullable(message, "message");
    nonNullable(exception, "exception");
    return new Or<>(this, One.none(), message, exception)::iterator;
  }

  default <E extends RuntimeException> One<T> or(final Suppl<? extends E> exception) {
    nonNullable(exception, "exception");
    return or("nothing", it -> exception.get());
  }
}

