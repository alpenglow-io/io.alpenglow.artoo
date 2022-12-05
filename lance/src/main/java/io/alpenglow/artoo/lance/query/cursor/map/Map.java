package io.alpenglow.artoo.lance.query.cursor.map;

import io.alpenglow.artoo.lance.func.TryFunction1;
import io.alpenglow.artoo.lance.query.Cursor;
import io.alpenglow.artoo.lance.query.cursor.Source;
import io.alpenglow.artoo.lance.query.cursor.routine.Routine;

public final class Map<OLD, NEW> implements Cursor<NEW> {
  private final Source<OLD> source;
  private final TryFunction1<? super OLD, ? extends NEW> map;

  public Map(final Source<OLD> source, final TryFunction1<? super OLD, ? extends NEW> map) {
    this.source = source;
    this.map = map;
  }

  @Override
  public NEW fetch() throws Throwable {
    final var fetched = source.fetch();
    return fetched != null ? map.tryApply(fetched) : null;
  }

  @Override
  public <P> Cursor<P> map(final TryFunction1<? super NEW, ? extends P> mapAgain) {
    return new Map<>(source, map.then(mapAgain));
  }

  @Override
  public boolean hasNext() {
    return source.hasNext();
  }

  @Override
  public <R1> R1 as(final Routine<NEW, R1> routine) {
    return routine.onSource().apply(this);
  }
}
