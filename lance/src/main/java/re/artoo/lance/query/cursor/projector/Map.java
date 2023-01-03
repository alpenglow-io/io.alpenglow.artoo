package re.artoo.lance.query.cursor.projector;

import re.artoo.lance.query.Closure;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Fetcher;
import re.artoo.lance.query.cursor.routine.Routine;

public final class Map<S, T> implements Cursor<T> {
  private final Fetcher<S> fetcher;
  private final Closure<? super S, ? extends T> map;

  public Map(final Fetcher<S> fetcher, final Closure<? super S, ? extends T> map) {
    this.fetcher = fetcher;
    this.map = map;
  }

  @Override
  public T fetch() throws Throwable {
    return map.invoke(fetcher.fetch());
  }

  @Override
  public <R> Cursor<R> map(final Closure<? super T, ? extends R> mapAgain) {
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
