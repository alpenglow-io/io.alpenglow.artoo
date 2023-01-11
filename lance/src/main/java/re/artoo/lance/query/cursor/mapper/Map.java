package re.artoo.lance.query.cursor.mapper;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.IntClosure;
import re.artoo.lance.query.cursor.Fetcher;
import re.artoo.lance.query.cursor.routine.Routine;

public final class Map<S, T> implements Cursor<T> {
  private final Fetcher<S> fetcher;
  private final IntClosure<? super S, ? extends T> map;

  public Map(final Fetcher<S> fetcher, final IntClosure<? super S, ? extends T> map) {
    this.fetcher = fetcher;
    this.map = map;
  }

  @Override
  public <R> R fetch(TryIntFunction1<? super T, ? extends R> detach) throws Throwable {
    return fetcher.fetch((index, element) -> detach.invoke(index, map.invoke(index, element)));
  }

  @Override
  public <R> Cursor<R> map(final IntClosure<? super T, ? extends R> mapAgain) {
    return new Map<>(fetcher, map.then(mapAgain));
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
