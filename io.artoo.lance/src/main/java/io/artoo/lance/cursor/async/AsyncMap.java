package io.artoo.lance.cursor.async;

import io.artoo.lance.func.Func;
import io.artoo.lance.cursor.Cursor;

final class AsyncMap<T, R> implements AsyncCursor<R> {
  private final AsyncCursor<T> cursor;
  private final Func.Uni<? super T, ? extends R> map;

  AsyncMap(final AsyncCursor<T> cursor, final Func.Uni<? super T, ? extends R> map) {
    this.cursor = cursor;
    this.map = map;
  }

  @Override
  public R fetch() throws Throwable {
    return map.tryApply(cursor.fetch());
  }

  @Override
  public boolean hasNext() {
    return false;
  }

  @Override
  public R next() {
    try {
      return fetch();
    } catch (Throwable throwable) {
      return null;
    }
  }

  @Override
  public <P> Cursor<P> map(final Func.Uni<? super R, ? extends P> mapAgain) {
    return new AsyncMap<>(cursor, map.then(mapAgain));
  }
}
