package artoo.query;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import artoo.query.one.Either;
import artoo.query.one.Filterable;
import artoo.query.one.Peekable;
import artoo.query.one.Projectable;
import artoo.query.one.Unwrappable;
import artoo.query.one.impl.Default;
import artoo.query.one.impl.Just;

public interface One<T> extends Projectable<T>, Peekable<T>, Filterable<T>, Either<T, One<T>>, Unwrappable<T> {
  static <L> One<L> of(final L value) {
    return value != null ? One.just(value) : One.none();
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
