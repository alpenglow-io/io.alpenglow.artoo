package oak.type;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNullElse;
import static oak.type.Varchar.string;

public interface Nullability {
  static <T> T nonNullable(final T any, final String argument) {
    if (isNull(any)) {
      throw new IllegalArgumentException(
        requireNonNullElse(
          argument,
          string("%s can't be null.").format(argument) + ""
        )
      );
    }
    return any;
  }
}
