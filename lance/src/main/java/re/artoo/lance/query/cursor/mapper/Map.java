package re.artoo.lance.query.cursor.mapper;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Fetcher;
import re.artoo.lance.query.cursor.routine.Routine;

public final class Map<S, T> implements Cursor<T> {
  private final Fetcher<S> fetcher;
  private final TryIntFunction1<? super S, ? extends T> map;

  public Map(final Fetcher<S> fetcher, final TryIntFunction1<? super S, ? extends T> map) {
    this.fetcher = fetcher;
    this.map = map;
  }

  @Override
  public <R> R fetch(TryIntFunction1<? super T, ? extends R> detach) throws Throwable {
    return fetcher.fetch((index, element) -> coalesce(index, coalesce(index, element, map), detach));
  }

  private static <Z, X> Z coalesce(int index, X value, TryIntFunction1<? super X, ? extends Z> func) throws Throwable {
    return value != null ? func.invoke(index, value) : null;
  }

  @Override
  public <R> Cursor<R> map(final TryIntFunction1<? super T, ? extends R> mapAgain) {
    return new Map<>(fetcher, (index, it) -> coalesce(index, coalesce(index, it, map), mapAgain));
  }

  @Override
  public boolean hasNext() {
    return fetcher.hasNext();
  }

  @Override
  public <R1> R1 as(final Routine<T, R1> routine) {
    return routine.onSource().apply(this);
  }
}
