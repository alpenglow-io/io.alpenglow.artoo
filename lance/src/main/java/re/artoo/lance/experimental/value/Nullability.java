package re.artoo.lance.experimental.value;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Objects.*;

public interface Nullability {
  static <T> T nonNullable(final T any, final String argument) {
    if (isNull(any))
      throw illegalArgument(argument);
    return any;
  }

  private static IllegalArgumentException illegalArgument(String argument) {
    return new IllegalArgumentException(
      requireNonNullElse(argument, """
        %s can't be null.
        """
        .formatted(argument)
      )
    );
  }

  static <T, R> R nonNullable(final T value, final String argument, final Function<T, R> then) {
    return nonNullable(then, "then").apply(nonNullable(value, argument));
  }

  static <R> R nonNullable(final Object[] values, final String[] arguments, final Supplier<R> suppl) {
    for (var index = 0; index < values.length; index++)
      if (values[index] == null)
        throw illegalArgument(arguments[index]);
    return suppl.get();
  }

  static boolean areNonNullable(final Object... arguments) {
    var areNonNullable = true;
    for (var i = 0; i < arguments.length && areNonNullable; i++) {
      try {
        nonNullable(arguments[i], "Arguments");
      } catch (IllegalArgumentException iae) {
        areNonNullable = false;
      }
    }
    return areNonNullable;
  }

  static <T> T nonNullableState(final T any, final String argument) {
    return nonNullableState(any, argument, "%s can't have a null-state.");
  }

  static <T> T nonNullableState(final T any, final String argument, final String formatted) {
    if (isNull(any)) {
      throw new IllegalStateException(
        String.format(
          nonNullable(formatted, "formatted"),
          nonNullable(argument, "argument")
        )
      );
    }
    return any;
  }

  static <T, R> R nullable(final T any, final Function<T, R> then, final Supplier<R> otherwise) {
    return nonNull(any)
      ? nonNullable(then, "then").apply(any)
      : nonNullable(otherwise, "otherwise").get();
  }

  static <T1, T2, R> R nullable(final T1 value1, final T2 value2, final BiFunction<T1, T2, R> then, final Supplier<R> otherwise) {
    return value1 == null && value2 == null
      ? nonNullable(otherwise, "otherwise").get()
      : nonNullable(then, "then").apply(value1, value2);
  }

  static <T, R> R nonNullable(final T any, final Function<T, R> then, final String message) {
    if (isNull(any))
      throw new IllegalStateException(requireNonNullElse(message, "Any is null"));
    return then.apply(any);
  }
}
