package re.artoo.lance.query.cursor;

import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.routine.Routine;

public final class Chain<T> implements Cursor<T> {
  private final Fetcher<T> prev;
  private final Fetcher<T> fetcher;

  public Chain(final Fetcher<T> prev, final Fetcher<T> fetcher) {
    this.prev = prev;
    this.fetcher = fetcher;
  }

  @Override
  public <R> R as(final Routine<T, R> routine) {
    return routine.onSource().apply(this);
  }

  @Override
  public T fetch() throws Throwable {
    return prev.hasNext() ? prev.fetch() : fetcher.fetch();
  }

  @Override
  public boolean hasNext() {
    return prev.hasNext() || fetcher.hasNext();
  }
}
