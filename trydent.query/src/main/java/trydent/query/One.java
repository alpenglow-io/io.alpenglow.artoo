package trydent.query;

import trydent.query.one.Either;
import trydent.query.one.Filterable;
import trydent.query.one.Peekable;
import trydent.query.one.Projectable;
import trydent.query.one.Unwrappable;
import trydent.query.one.internal.Default;
import trydent.query.one.internal.Just;
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
