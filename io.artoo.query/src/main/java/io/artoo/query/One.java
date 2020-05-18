package io.artoo.query;

import io.artoo.cursor.Cursor;
import io.artoo.query.one.Otherwise;
import io.artoo.query.one.Filterable;
import io.artoo.query.one.Peekable;
import io.artoo.query.one.Projectable;
import io.artoo.type.AsByte;
import io.artoo.type.AsDouble;
import io.artoo.type.AsInt;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public interface One<R extends Record> extends Projectable<R>, Peekable<R>, Filterable<R>, Otherwise<R> {
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
    return (One<L>) OneRecord.None;
  }
}

final class Just<T extends Record> implements One<T> {
  private final T value;

  public Just(T value) {this.value = value;}

  @NotNull
  @Override
  public Iterator<T> iterator() {
    return Cursor.of(value);
  }
}

enum OneRecord implements One<Record> {
  None;

  @NotNull
  @Override
  public Iterator<Record> iterator() {
    return Cursor.none();
  }
}
