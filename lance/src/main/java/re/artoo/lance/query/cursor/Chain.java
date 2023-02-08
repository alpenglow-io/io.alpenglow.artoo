package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;

public final class Chain<T> implements Cursor<T> {
  private final Head<T> prev;
  private final Head<T> next;

  public Chain(final Head<T> prev, final Head<T> next) {
    this.prev = prev;
    this.next = next;
  }

  @Override
  public <R> R scroll(TryIntFunction1<? super T, ? extends R> fetch) throws Throwable {
    return prev.hasNext() ? prev.scroll(fetch) : next.scroll(fetch);
  }

  @Override
  public boolean hasNext() {
    return prev.hasNext() || next.hasNext();
  }
}
