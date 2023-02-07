package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;

public final class Chain<T> implements Cursor<T> {
  private final Probe<T> prev;
  private final Probe<T> next;

  public Chain(final Probe<T> prev, final Probe<T> next) {
    this.prev = prev;
    this.next = next;
  }

  @Override
  public <R> R tick(TryIntFunction1<? super T, ? extends R> fetch) throws Throwable {
    return prev.hasNext() ? prev.tick(fetch) : next.tick(fetch);
  }

  @Override
  public boolean hasNext() {
    return prev.hasNext() || next.hasNext();
  }
}
