package io.alpenglow.artoo.lance.query.cursor;

import io.alpenglow.artoo.lance.query.Cursor;
import io.alpenglow.artoo.lance.query.cursor.routine.Routine;

public final class Link<T> implements Cursor<T> {
  private final Source<T> prev;
  private final Source<T> source;

  public Link(final Source<T> prev, final Source<T> source) {
    this.prev = prev;
    this.source = source;
  }

  @Override
  public <R> R as(final Routine<T, R> routine) {
    return routine.onSource().apply(this);
  }

  @Override
  public T fetch() throws Throwable {
    return prev.hasNext() ? prev.fetch() : source.fetch();
  }

  @Override
  public boolean hasNext() {
    return prev.hasNext() || source.hasNext();
  }
}
