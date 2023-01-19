package re.artoo.lance.query.cursor.mapper;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Inquiry;
import re.artoo.lance.query.cursor.routine.Routine;

public final class Map<S, T> implements Cursor<T> {
  private final Inquiry<S> inquiry;
  private final TryIntFunction1<? super S, ? extends T> map;

  public Map(final Inquiry<S> inquiry, final TryIntFunction1<? super S, ? extends T> map) {
    this.inquiry = inquiry;
    this.map = map;
  }

  @Override
  public <R> R traverse(TryIntFunction1<? super T, ? extends R> fetch) throws Throwable {
    return inquiry.traverse((index, element) -> coalesce(index, coalesce(index, element, map), fetch));
  }

  private static <Z, X> Z coalesce(int index, X value, TryIntFunction1<? super X, ? extends Z> func) throws Throwable {
    return value != null ? func.invoke(index, value) : null;
  }

  @Override
  public <R> Cursor<R> map(final TryIntFunction1<? super T, ? extends R> mapAgain) {
    return new Map<>(inquiry, (index, it) -> coalesce(index, coalesce(index, it, map), mapAgain));
  }

  @Override
  public boolean hasNext() {
    return inquiry.hasNext();
  }

  @Override
  public <R1> R1 as(final Routine<T, R1> routine) {
    return routine.onSource().apply(this);
  }
}
