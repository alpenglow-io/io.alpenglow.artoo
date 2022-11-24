package lance.literator.cursor;

import lance.literator.Cursor;
import lance.literator.Literator;
import lance.literator.cursor.routine.Routine;

public interface Closable<T> extends Literator<T> {
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


