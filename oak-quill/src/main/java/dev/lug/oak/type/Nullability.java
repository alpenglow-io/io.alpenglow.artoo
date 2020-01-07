package dev.lug.oak.type;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static dev.lug.oak.type.Str.str;
import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNullElse;

public interface Nullability {
  @Contract("_, _ -> param1")
  static <T> T nonNullable(final T any, final String argument) {
    if (isNull(any)) {
      throw new IllegalArgumentException(
        requireNonNullElse(
          argument,
          str("%s can't be null.").format(argument) + ""
        )
      );
    }
    return any;
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
        str(nonNullable(formatted, "formatted")).format(nonNullable(argument, "argument")) + ""
      );
    }
    return any;
  }
}
