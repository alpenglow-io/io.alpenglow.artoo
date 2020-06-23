package io.artoo.lance.query;

import io.artoo.lance.cursor.Cursor;
import io.artoo.lance.query.one.Filterable;
import io.artoo.lance.query.one.Otherwise;
import io.artoo.lance.query.one.Peekable;
import io.artoo.lance.query.one.Projectable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public interface One<T> extends Projectable<T>, Peekable<T>, Filterable<T>, Otherwise<T> {
  static <T> One<T> of(final T element) {
    return element != null ? One.lone(element) : One.none();
  }

  @NotNull
  @Contract("_ -> new")
  static <L> One<L> lone(final L record) {
    return new Lone<>(record);
  }

  @NotNull
  @Contract(value = " -> new", pure = true)
  @SuppressWarnings("unchecked")
  static <L> One<L> none() {
    return (One<L>) NoRecord.None;
  }

  default T yield() {
    return iterator().next();
  }
}

record Lone<T>(T element) implements One<T> {
  public Lone { assert element != null; }

  @NotNull
  @Override
  public Iterator<T> iterator() {
    return Cursor.lone(element);
  }
}

record Done(Throwable throwable) implements One<Throwable> {
  @NotNull
  @Override
  public Iterator<Throwable> iterator() {
    return Cursor.lone(throwable);
  }
}

enum NoRecord implements One<Record> {
  None;

  @NotNull
  @Override
  public Iterator<Record> iterator() {
    return Cursor.none();
  }
}
