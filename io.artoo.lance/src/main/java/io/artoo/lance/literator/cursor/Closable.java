package io.artoo.lance.literator.cursor;

import io.artoo.lance.literator.Cursor;
import io.artoo.lance.literator.Literator;
import io.artoo.lance.literator.cursor.routine.Routine;

public interface Closable<T> extends Literator<T> {
  default Cursor<T> close() {
    return new Close<>(this);
  }

  default Cursor<T> scroll() {
    T element = null;
    while (hasNext()) {
      element = next();
    }

    return Cursor.maybe(element);
  }
}

final class Close<T> implements Cursor<T> {
  private T closed = null;
  private final Literator<T> source;

  Close(Literator<T> source) {
    this.source = source;
  }

  @Override
  public T fetch() {
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
  public T next() {
    try {
      return hasNext() ? closed : null;
    } finally {
      closed = null;
    }
  }

  @Override
  public <R> R as(final Routine<T, R> routine) {
    return source instanceof Cursor<T> cursor ? cursor.as(routine) : Cursor.<T>nothing().as(routine);
  }
}

final class Closed<T> {
  private T fetched;

  @SuppressWarnings("ResultOfMethodCallIgnored")
  private T fetched(Literator<T> literator) {
    if (fetched == null) literator.hasNext();
    return fetched;
  }

  public final T fetched() { return fetched; }
  public final void fetched(T value) { this.fetched = value; }
}


