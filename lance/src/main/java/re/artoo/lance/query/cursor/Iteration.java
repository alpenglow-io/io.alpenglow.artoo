package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.routine.Routine;

import java.util.Collection;
import java.util.Iterator;

public final class Iteration<T> implements Cursor<T> {
  private final Collection<T> collection;
  private int index = 0;

  public Iteration(final Collection<T> collection) {this.collection = collection;}

  @Override
  public <R> R tick(TryIntFunction1<? super T, ? extends R> fetch) throws Throwable {
    return fetch.invoke(index++, collection.);
  }

  @Override
  public <R> R as(final Routine<T, R> routine) {
    return routine.onIterator().apply(iterator);
  }

  @Override
  public boolean hasNext() {
    return iterator.hasNext();
  }

  @Override
  public Probe<T> reverse() {
    return Cursor.super.reverse();
  }
}
