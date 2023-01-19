package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.routine.Routine;

import java.util.Iterator;

public final class Iteration<T> implements Cursor<T> {
  private final Iterator<T> iterator;
  private int index = 0;

  public Iteration(final Iterator<T> iterator) {this.iterator = iterator;}

  @Override
  public <R> R traverse(TryIntFunction1<? super T, ? extends R> fetch) throws Throwable {
    return fetch.invoke(index++, iterator.next());
  }

  @Override
  public <R> R as(final Routine<T, R> routine) {
    return routine.onIterator().apply(iterator);
  }

  @Override
  public boolean hasNext() {
    return iterator.hasNext();
  }
}
