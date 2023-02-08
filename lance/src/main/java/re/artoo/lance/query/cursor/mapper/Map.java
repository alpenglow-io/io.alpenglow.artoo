package re.artoo.lance.query.cursor.mapper;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Head;

public final class Map<S, T> implements Cursor<T> {
  private final Head<S> head;
  private final TryIntFunction1<? super S, ? extends T> map;

  public Map(final Head<S> head, final TryIntFunction1<? super S, ? extends T> map) {
    this.head = head;
    this.map = map;
  }

  @Override
  public <R> R scroll(TryIntFunction1<? super T, ? extends R> fetch) throws Throwable {
    return head.scroll((index, element) -> coalesce(index, coalesce(index, element, map), fetch));
  }

  private static <Z, X> Z coalesce(int index, X value, TryIntFunction1<? super X, ? extends Z> func) throws Throwable {
    return value != null ? func.invoke(index, value) : null;
  }

  @Override
  public <R> Cursor<R> map(final TryIntFunction1<? super T, ? extends R> mapAgain) {
    return new Map<>(head, (index, it) -> coalesce(index, coalesce(index, it, map), mapAgain));
  }

  @Override
  public boolean hasNext() {
    return head.hasNext();
  }

}
