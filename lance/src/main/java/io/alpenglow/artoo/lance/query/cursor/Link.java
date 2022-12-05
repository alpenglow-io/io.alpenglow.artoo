package io.alpenglow.artoo.lance.query.cursor;

import io.alpenglow.artoo.lance.query.Cursor;
import io.alpenglow.artoo.lance.query.Repeatable;
import io.alpenglow.artoo.lance.query.cursor.routine.Routine;

public final class Link<T> implements Cursor<T> {
  private final Repeatable<T> prev;
  private final Repeatable<T> repeatable;

  public Link(final Repeatable<T> prev, final Repeatable<T> repeatable) {
    this.prev = prev;
    this.repeatable = repeatable;
  }

  @Override
  public <R> R as(final Routine<T, R> routine) {
    return routine.onLiterator().apply(this);
  }

  @Override
  public T fetch() throws Throwable {
    return prev.hasNext() ? prev.fetch() : repeatable.fetch();
  }

  @Override
  public boolean hasNext() {
    return prev.hasNext() || repeatable.hasNext();
  }
}
