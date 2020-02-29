package oak.query.one;

import oak.cursor.Cursor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.nonNull;
import static oak.type.Nullability.nonNullable;

public interface One<T> extends Projectable<T>, Filterable<T>, Either<T>, Hookable<T>, Unwrappable<T> {
  static <L> One<L> of(final L value) {
    return value != null ? One.just(value) : One.none();
  }

  @NotNull
  @Contract("_ -> new")
  static <L> One<L> just(final L value) {
    return () -> Cursor.of(value);
  }

  @NotNull
  @Contract(value = " -> new", pure = true)
  static <L> One<L> none() {
    return Cursor::none;
  }
}
