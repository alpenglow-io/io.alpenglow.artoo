package oak.type;

import oak.func.Func;
import oak.func.Sup;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static oak.type.Str.str;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNullElse;

public interface Nullability {
  @Contract("_, _ -> param1")
  static <T> T nonNullable(final T any, final String argument) {
    if (isNull(any)) throw illegalArgument(argument);
    return any;
  }

  @NotNull
  private static IllegalArgumentException illegalArgument(String argument) {
    return new IllegalArgumentException(
      requireNonNullElse(
        argument,
        str("%s can't be null.").format(argument) + ""
      )
    );
  }

  static <T, R> R nonNullable(final T value, @NotNull final String argument, @NotNull final Func<T, R> then) {
    return nonNullable(then, "then").apply(nonNullable(value, argument));
  }

  static <R> R nonNullable(final Object[] values, @NotNull final String[] arguments, final Sup<R> sup) {
    for (var index = 0; index < values.length; index++) if (values[index] == null) throw illegalArgument(arguments[index]);
    return sup.get();
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

  static <T, R> R nullable(final T any, final Func<T, R> then, final Sup<R> otherwise) {
    return nonNull(any)
      ? nonNullable(then, "then").apply(any)
      : nonNullable(otherwise, "otherwise").get();
  }

  static <T, R> R nonNullable(final T any, final Func<T, R> then, final String message) {
    if (isNull(any)) throw new IllegalStateException(requireNonNullElse(message, "Any is null"));
    return then.apply(any);
  }
}
