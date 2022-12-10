package io.alpenglow.artoo.lance.query.cursor;

import io.alpenglow.artoo.lance.query.Cursor;
import io.alpenglow.artoo.lance.query.cursor.routine.Routine;

import java.util.Iterator;

public interface Closer<T> extends Fetcher<T> {
  default Cursor<T> close() {
    return new Close<>(this);
  }

  default Cursor<T> keepNull() {
    T element = null;
    while (hasNext()) {
      element = next();
    }

    return Cursor.maybe(element);
  }

  default Cursor<T> skipNull() {
    T element = null;
    while (hasNext()) {
      final var next = next();
      element = next != null ? next : element;
    }

    return Cursor.maybe(element);
  }
}

final class Close<VALUE> implements Cursor<VALUE> {
  private VALUE closed = null;
  private final Iterator<VALUE> source;

  Close(Iterator<VALUE> source) {
    this.source = source;
  }

  @Override
  public VALUE fetch() {
    return next();
  }

  @Override
  public boolean hasNext() {
    var hasNext = closed != null || source.hasNext();
    while (hasNext && closed == null) {
      closed = source.next();
      hasNext = closed != null || source.hasNext();
    }
    return hasNext;
  }

  @Override
  public VALUE next() {
    try {
      return hasNext() ? closed : null;
    } finally {
      closed = null;
    }
  }

  @Override
  public <R> R as(final Routine<VALUE, R> routine) {
    return source instanceof Cursor<VALUE> cursor ? cursor.as(routine) : Cursor.<VALUE>empty().as(routine);
  }
}


