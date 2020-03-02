package oak.query.one;

import oak.cursor.Cursor;
import oak.func.$2.IntCons;
import oak.query.Queryable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface One<T> extends Projectable<T>, Filterable<T>, Either<T>, Unwrappable<T> {
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
  @SuppressWarnings("unchecked")
  static <L> One<L> none() {
    return (One<L>) Default.None;
  }
}
