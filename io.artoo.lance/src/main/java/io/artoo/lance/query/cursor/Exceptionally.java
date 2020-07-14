package io.artoo.lance.query.cursor;

import io.artoo.lance.func.Cons;

final class Exceptionally<R> implements Cursor<R> {
  private final Cursor<R> cursor;
  private final Cons.Uni<? super Throwable> catch$;

  Exceptionally(final Cursor<R> cursor, final Cons.Uni<? super Throwable> catch$) {

    this.cursor = cursor;
    this.catch$ = catch$;
  }

  @Override
  public R fetch() {
    try {
      return cursor.fetch();
    } catch (Throwable throwable) {
      catch$.accept(throwable);
      return null;
    }
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
      catch$.accept(throwable);
      return null;
    }
  }
}
