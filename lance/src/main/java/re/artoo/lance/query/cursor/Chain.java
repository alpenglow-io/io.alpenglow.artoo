package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.routine.Routine;

public final class Chain<T> implements Cursor<T> {
  private final Fetcher<T> prev;
  private final Fetcher<T> next;

  public Chain(final Fetcher<T> prev, final Fetcher<T> next) {
    this.prev = prev;
    this.next = next;
  }

  @Override
  public <R> R as(final Routine<T, R> routine) {
    return routine.onSource().apply(this);
  }

  @Override
  public <R> R fetch(TryIntFunction1<? super T, ? extends R> detach) throws Throwable {
    return prev.hasNext() ? prev.fetch(detach) : next.fetch(detach);
  }

  @Override
  public boolean hasNext() {
    return prev.hasNext() || next.hasNext();
  }
}
