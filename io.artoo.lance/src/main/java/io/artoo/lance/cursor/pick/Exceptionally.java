package io.artoo.lance.cursor.pick;

import io.artoo.lance.cursor.Cursor;
import io.artoo.lance.func.Cons;

public final class Exceptionally<R> implements Cursor<R> {
  private final Cursor<R> cursor;
  private final Cons.Uni<? super Throwable> catch$;

  public Exceptionally(final Cursor<R> cursor, final Cons.Uni<? super Throwable> catch$) {
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
