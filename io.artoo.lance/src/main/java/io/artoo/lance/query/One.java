package io.artoo.lance.query;

import io.artoo.lance.query.cursor.Cursor;
import io.artoo.lance.query.one.Filterable;
import io.artoo.lance.query.one.Otherwise;
import io.artoo.lance.query.one.Peekable;
import io.artoo.lance.query.one.Projectable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface One<T> extends Projectable<T>, Peekable<T>, Filterable<T>, Otherwise<T> {
  static <T> One<T> of(final T element) {
    return element != null ? One.lone(element) : One.none();
  }

  @NotNull
  @Contract("_ -> new")
  static <L> One<L> lone(final L element) {
    return new Lone<>(element);
  }

  @NotNull
  @Contract(value = " -> new", pure = true)
  @SuppressWarnings("unchecked")
  static <L> One<L> none() {
    return (One<L>) NoRecord.None;
  }

  default T yield() {
    return cursor().next();
  }
}

record Lone<T>(T element) implements One<T> {
  public Lone { assert element != null; }

  @NotNull
  @Override
  public Cursor<T> cursor() {
    return Cursor.local(element);
  }
}

enum NoRecord implements One<Record> {
  None;

  @NotNull
  @Override
  public Cursor<Record> cursor() {
    return Cursor.local();
  }
}
