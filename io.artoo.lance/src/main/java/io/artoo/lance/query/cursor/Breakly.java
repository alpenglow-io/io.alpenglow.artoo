package io.artoo.lance.query.cursor;

import io.artoo.lance.func.Cons;

final class Breakly<T> implements Cursor<T> {
  private final Throwable throwable;
  private final Cons.Uni<? super Throwable> catch$;

  Breakly(final Throwable throwable, final Cons.Uni<? super Throwable> catch$) {
    assert throwable != null && catch$ != null;
    this.throwable = throwable;
    this.catch$ = catch$;
  }

  @Override
  public T fetch() {
    catch$.accept(throwable);
    return null;
  }

  @Override
  public boolean hasNext() {
    catch$.accept(throwable);
    return false;
  }

  @Override
  public T next() {
    return fetch();
  }
}
