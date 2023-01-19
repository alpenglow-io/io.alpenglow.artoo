package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.routine.Routine;

import java.util.Iterator;

public interface Closer<T> extends Inquiry<T> {
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
  private int index = 0;
  private final Iterator<T> source;

  Close(Iterator<T> source) {
    this.source = source;
  }

  @Override
  public <R> R traverse(TryIntFunction1<? super T, ? extends R> fetch) throws Throwable {
    return fetch.invoke(index++, next());
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
    return source instanceof Cursor<T> cursor ? cursor.as(routine) : Cursor.<T>empty().as(routine);
  }
}


