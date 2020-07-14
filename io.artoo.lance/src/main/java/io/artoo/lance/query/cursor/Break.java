package io.artoo.lance.query.cursor;

import io.artoo.lance.func.Cons;

final class Break<T> implements Cursor<T> {
  private final Throwable throwable;

  Break(final Throwable throwable) {
    assert throwable != null;
    this.throwable = throwable;
  }

  @Override
  public Cursor<T> exceptionally(final Cons.Uni<? super Throwable> catch$) {
    return new Breakly<>(throwable, catch$);
  }

  @Override
  public T fetch() {
    return null;
  }

  @Override
  public boolean hasNext() {
    return false;
  }

  @Override
  public T next() {
    return fetch();
  }
}
