package io.artoor.query;

import io.artoor.cursor.Cursor;
import io.artoor.query.one.Otherwise;
import io.artoor.query.one.Filterable;
import io.artoor.query.one.Peekable;
import io.artoor.query.one.Projectable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

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
