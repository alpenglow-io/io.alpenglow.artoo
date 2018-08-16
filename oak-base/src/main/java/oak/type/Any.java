package oak.type;

import oak.func.fun.Function1;
import oak.func.sup.Supplier1;

import java.util.Objects;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNullElse;

public interface Any {
  static <T, R> R nonNull(final T any, final Function1<T, R> then) {
    return nonNull(any, then, null);
  }
  static <T, R> R nonNull(final T any, final Function1<T, R> then, final String message) {
    if (isNull(any)) throw new IllegalStateException(requireNonNullElse(message, "Any is null"));

    return then.apply(any);
  }
  static <T, R> R nonNullOrElse(final T any, final Function1<T, R> then, final Supplier1<R> otherwise) {
    return Objects.nonNull(any)
      ? Objects.requireNonNull(then, "Then is null").apply(any)
      : Objects.requireNonNull(otherwise, "Otherwise is null").get();
  }
}
