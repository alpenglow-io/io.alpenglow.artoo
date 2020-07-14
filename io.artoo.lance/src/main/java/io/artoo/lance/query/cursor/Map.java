package io.artoo.lance.query.cursor;

import io.artoo.lance.func.Func;

final class Map<T, R> implements Cursor<R> {
  private final Cursor<T> cursor;
  private final Func.Uni<? super T, ? extends R> map;

  Map(final Cursor<T> cursor, final Func.Uni<? super T, ? extends R> map) {
    assert cursor != null && map != null;
    this.cursor = cursor;
    this.map = map;
  }

  @Override
  public final R fetch() throws Throwable {
    return map.tryApply(cursor.fetch());
  }

  @Override
  public <P> Cursor<P> map(final Func.Uni<? super R, ? extends P> mapAgain) {
    return new Map<>(cursor, map.then(mapAgain));
  }

  @Override
  public boolean hasNext() {
    return cursor.hasNext();
  }

  @Override
  public R next() {
    try {
      return fetch();
    } catch (Throwable throwable) {
      return null;
    }
  }
}
