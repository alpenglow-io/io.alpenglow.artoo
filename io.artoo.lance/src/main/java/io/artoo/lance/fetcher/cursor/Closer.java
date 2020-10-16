package io.artoo.lance.fetcher.cursor;

import io.artoo.lance.fetcher.Cursor;
import io.artoo.lance.fetcher.Fetcher;
import io.artoo.lance.fetcher.routine.Routine;

public interface Closer<T> extends Fetcher<T> {
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
  private final Fetcher<T> source;

  Close(Fetcher<T> source) {
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
  public final <R> Cursor<R> invoke(final Routine<T, R> routine) {
    return source instanceof Cursor<T> cursor ? cursor.invoke(routine) : Cursor.nothing();
  }
}

final class Closed<T> {
  private T fetched;

  @SuppressWarnings("ResultOfMethodCallIgnored")
  private T fetched(Fetcher<T> fetcher) {
    if (fetched == null) fetcher.hasNext();
    return fetched;
  }

  public final T fetched() { return fetched; }
  public final void fetched(T value) { this.fetched = value; }
}


