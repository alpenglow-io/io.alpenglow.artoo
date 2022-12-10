package io.alpenglow.artoo.lance.query.cursor.projector;

import io.alpenglow.artoo.lance.func.TryFunction1;
import io.alpenglow.artoo.lance.query.Cursor;
import io.alpenglow.artoo.lance.query.cursor.Fetcher;
import io.alpenglow.artoo.lance.query.cursor.routine.Routine;

public final class Map<FROM, TO> implements Cursor<TO> {
  private final Fetcher<FROM> fetcher;
  private final TryFunction1<? super FROM, ? extends TO> map;

  public Map(final Fetcher<FROM> fetcher, final TryFunction1<? super FROM, ? extends TO> map) {
    this.fetcher = fetcher;
    this.map = map;
  }

  @Override
  public TO fetch() throws Throwable {
    final var fetched = fetcher.fetch();
    return fetched != null ? map.tryApply(fetched) : null;
  }

  @Override
  public <P> Cursor<P> map(final TryFunction1<? super TO, ? extends P> mapAgain) {
    return new Map<>(fetcher, map.then(mapAgain));
  }

  @Override
  public boolean hasNext() {
    return fetcher.hasNext();
  }

  @Override
  public <R1> R1 as(final Routine<TO, R1> routine) {
    return routine.onSource().apply(this);
  }
}
