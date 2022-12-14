package io.alpenglow.artoo.lance.query.cursor.projector;

import io.alpenglow.artoo.lance.query.Closure;
import io.alpenglow.artoo.lance.query.Cursor;
import io.alpenglow.artoo.lance.query.Unit;
import io.alpenglow.artoo.lance.query.cursor.Fetcher;
import io.alpenglow.artoo.lance.query.cursor.routine.Routine;

public final class Map<SOURCE, MIDDLE> implements Cursor<MIDDLE> {
  private final Fetcher<SOURCE> fetcher;
  private final Closure<SOURCE, MIDDLE> map;

  public Map(final Fetcher<SOURCE> fetcher, final Closure<SOURCE, MIDDLE> map) {
    this.fetcher = fetcher;
    this.map = map;
  }

  @Override
  public Unit<MIDDLE> fetch() throws Throwable {
    Unit<SOURCE> fetch = fetcher.fetch();
    return map.invoke(fetch);
  }

  @Override
  public <TARGET> Cursor<TARGET> map(final Closure<MIDDLE, TARGET> mapAgain) {
    return new Map<>(fetcher, map.then(mapAgain));
  }

  @Override
  public boolean hasNext() {
    return fetcher.hasNext();
  }

  @Override
  public <R1> R1 as(final Routine<MIDDLE, R1> routine) {
    return routine.onSource().apply(this);
  }
}
