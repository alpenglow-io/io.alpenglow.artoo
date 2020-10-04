package io.artoo.lance.query;

import io.artoo.lance.fetcher.Cursor;
import io.artoo.lance.query.one.Filterable;
import io.artoo.lance.query.one.Otherwise;
import io.artoo.lance.query.one.Peekable;
import io.artoo.lance.query.one.Projectable;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public interface One<T> extends Projectable<T>, Peekable<T>, Filterable<T>, Otherwise<T> {
  static <T> One<T> of(final T element) {
    return element != null ? One.lone(element) : One.none();
  }

  static <L> One<L> lone(final L element) {
    return new Lone<>(element);
  }

  @SuppressWarnings("unchecked")
  static <L> One<L> none() {
    return (One<L>) None.Default;
  }

  @Deprecated(forRemoval = true)
  default T yield() {
    return iterator().next();
  }

  record Lone<T>(T element) implements One<T> {
    public Lone { assert element != null; }

    @Override
    public final Cursor<T> cursor() {
      return Cursor.open(element);
    }
  }

  enum None implements One<Object> {
    Default;

    @Override
    public final Cursor<Object> cursor() {
      return Cursor.nothing();
    }
  }
}
