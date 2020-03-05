package oak.query;

import oak.query.one.Either;
import oak.query.one.Filterable;
import oak.query.one.Peekable;
import oak.query.one.Projectable;
import oak.query.one.Unwrappable;
import oak.query.one.internal.Default;
import oak.query.one.internal.Just;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface One<T> extends Projectable<T>, Peekable<T>, Filterable<T>, Either<T>, Unwrappable<T> {
  static <L> One<L> of(final L value) {
    return value != null ? One.just(value) : One.none();
  }

  static OneInt of(final int value) {
    return null;
  }

  @NotNull
  @Contract("_ -> new")
  static <L> One<L> just(final L value) {
    return new Just<>(value);
  }

  @NotNull
  @Contract(value = " -> new", pure = true)
  @SuppressWarnings("unchecked")
  static <L> One<L> none() {
    return (One<L>) Default.None;
  }
}
