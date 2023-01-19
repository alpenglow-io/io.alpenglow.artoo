package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.routine.Routine;

public final class Chain<T> implements Cursor<T> {
  private final Inquiry<T> prev;
  private final Inquiry<T> next;

  public Chain(final Inquiry<T> prev, final Inquiry<T> next) {
    this.prev = prev;
    this.next = next;
  }

  @Override
  public <R> R as(final Routine<T, R> routine) {
    return routine.onSource().apply(this);
  }

  @Override
  public <R> R traverse(TryIntFunction1<? super T, ? extends R> fetch) throws Throwable {
    return prev.hasNext() ? prev.traverse(fetch) : next.traverse(fetch);
  }

  @Override
  public boolean hasNext() {
    return prev.hasNext() || next.hasNext();
  }
}
