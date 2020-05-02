package io.artoo.query;

import io.artoo.query.one.Either;
import io.artoo.query.one.Filterable;
import io.artoo.query.one.Peekable;
import io.artoo.query.one.Projectable;
import io.artoo.query.one.impl.Default;
import io.artoo.query.one.impl.Just;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface One<R extends Record> extends Projectable<R>, Peekable<R>, Filterable<R>, Either<R>, Queryable<R> {
  static <L extends  Record> One<L> of(final L record) {
    return record != null ? One.just(record) : One.none();
  }

  @NotNull
  @Contract("_ -> new")
  static <L extends Record> One<L> just(final L record) {
    return new Just<>(record);
  }

  @NotNull
  @Contract(value = " -> new", pure = true)
  @SuppressWarnings("unchecked")
  static <L extends Record> One<L> none() {
    return (One<L>) Default.None;
  }
}
