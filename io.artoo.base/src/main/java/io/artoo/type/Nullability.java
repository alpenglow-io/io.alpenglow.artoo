package io.artoo.type;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import io.artoo.func.Func;
import io.artoo.func.Suppl;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNullElse;
import static io.artoo.type.Str.$;

public interface Nullability {
  @Contract("_, _ -> param1")
  static <T> T nonNullable(final T any, final String argument) {
    if (isNull(any))
      throw illegalArgument(argument);
    return any;
  }

  @NotNull
  private static IllegalArgumentException illegalArgument(String argument) {
    return new IllegalArgumentException(
      requireNonNullElse(
        argument,
        $("%s can't be null.").format(argument) + ""
      )
    );
  }

  static <T, R> R nonNullable(final T value, @NotNull final String argument, @NotNull final Func<T, R> then) {
    return nonNullable(then, "then").apply(nonNullable(value, argument));
  }

  static <R> R nonNullable(final Object[] values, @NotNull final String[] arguments, final Suppl<R> suppl) {
    for (var index = 0; index < values.length; index++)
      if (values[index] == null)
        throw illegalArgument(arguments[index]);
    return suppl.get();
  }

  static boolean areNonNullable(@NotNull final Object... arguments) {
    var areNonNullable = true;
    for (int i = 0; i < arguments.length && areNonNullable; i++) {
      try {
        nonNullable(arguments[i], "Arguments");
      } catch (IllegalArgumentException iae) {
        areNonNullable = false;
      }
    }
    return areNonNullable;
  }

  @Contract("_, _ -> param1")
  static <T> T nonNullableState(final T any, final String argument) {
    return nonNullableState(any, argument, "%s can't have a null-state.");
  }

  @Contract("_, _, _ -> param1")
  static <T> T nonNullableState(final T any, final String argument, final String formatted) {
    if (isNull(any)) {
      throw new IllegalStateException(
        $(nonNullable(formatted, "formatted")).format(nonNullable(argument, "argument")) + ""
      );
    }
    return any;
  }

  static <T, R> R nullable(final T any, final Func<T, R> then, final Suppl<R> otherwise) {
    return nonNull(any)
      ? nonNullable(then, "then").apply(any)
      : nonNullable(otherwise, "otherwise").get();
  }

  static <T1, T2, R> R nullable(final T1 value1, final T2 value2, final io.artoo.func.$2.Func<T1, T2, R> then, final Suppl<R> otherwise) {
    return value1 == null && value2 == null
      ? nonNullable(otherwise, "otherwise").get()
      : nonNullable(then, "then").apply(value1, value2);
  }

  static <T1, T2, T3, R> R nullable(final T1 value1, final T2 value2, final T3 value3, final io.artoo.func.$3.Func<T1, T2, T3, R> then, final Suppl<R> otherwise) {
    return value1 == null && value2 == null && value3 == null
      ? nonNullable(otherwise, "otherwise").get()
      : nonNullable(then, "then").apply(value1, value2, value3);
  }

  static <T, R> R nonNullable(final T any, final Func<T, R> then, final String message) {
    if (isNull(any))
      throw new IllegalStateException(requireNonNullElse(message, "Any is null"));
    return then.apply(any);
  }
}
