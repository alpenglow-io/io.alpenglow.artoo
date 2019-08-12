package oak.type;

import oak.collect.query.Maybe;
import oak.func.fun.Function1;
import oak.func.pre.Predicate1;
import oak.func.sup.Supplier1;

import java.util.Objects;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;
import static java.util.Objects.requireNonNullElse;
import static oak.collect.query.Maybe.just;
import static oak.collect.query.Maybe.maybe;
import static oak.collect.query.Maybe.none;

public interface Any<T> extends Supplier1<T> {
  static <T, R> R nonNull(final T any, final Function1<T, R> then) {
    return nonNull(any, then, null);
  }
  static <T, R> R nonNull(final T any, final Function1<T, R> then, final String message) {
    if (isNull(any)) throw new IllegalStateException(requireNonNullElse(message, "Any is null"));

    return then.apply(any);
  }
  static <T, R> R nonNullOrElse(final T any, final Function1<T, R> then, final Supplier1<R> otherwise) {
    return Objects.nonNull(any)
      ? requireNonNull(then, "Then is null").apply(any)
      : requireNonNull(otherwise, "Otherwise is null").get();
  }

  default Maybe<T> filter(final Predicate1<T> predicate) {
    maybe(get()).where(predicate);
    requireNonNull(get());
    requireNonNull(predicate);
    return predicate.apply(get()) ? just(get()) : none();
  }
}
