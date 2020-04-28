package io.artoo.query;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import io.artoo.query.one.Either;
import io.artoo.query.one.Filterable;
import io.artoo.query.one.Peekable;
import io.artoo.query.one.Projectable;
import io.artoo.query.one.Unwrappable;
import io.artoo.query.one.impl.Default;
import io.artoo.query.one.impl.Just;

public interface One<T> extends Projectable<T>, Peekable<T>, Filterable<T>, Either<T>, Unwrappable<T> {
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
