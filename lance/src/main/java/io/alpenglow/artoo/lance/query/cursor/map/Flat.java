package io.alpenglow.artoo.lance.query.cursor.map;

import io.alpenglow.artoo.lance.query.Cursor;
import io.alpenglow.artoo.lance.query.cursor.Source;
import io.alpenglow.artoo.lance.query.cursor.routine.Routine;

public final class Flat<T> implements Cursor<T> {
  private final Flatten<T> flatten = new Flatten<>();
  private final Source<Source<T>> source;

  public Flat(final Source<Source<T>> source) {
    this.source = source;
  }

  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public T fetch() throws Throwable {
    T element = null;
    while (hasNext() && (element = flatten.source().fetch()) == null)
      ;
    return element;
  }

  @Override
  public boolean hasNext() {
    flatten.hasNext(source.hasNext() || (flatten.source() != null && flatten.source().hasNext()));

    if (flatten.hasNext() && (flatten.source() == null || !flatten.source().hasNext())) {
      flatten.source(source.next());
    }

    return flatten.hasNext();
  }

  @Override
  public <R> R as(final Routine<T, R> routine) {
    return routine.onSource().apply(this);
  }
}
