package io.artoo.lance.query.cursor;

import io.artoo.lance.func.Cons;

final class Beep<R> implements Cursor<R> {
  private final Cursor<R> cursor;
  private final Cons.Uni<Throwable> beep;

  Beep(final Cursor<R> cursor, final Cons.Uni<Throwable> beep) {
    assert cursor != null && beep != null;
    this.cursor = cursor;
    this.beep = beep;
  }

  private void beep() {
    final var cause = cursor.cause();
    if (cause != null) beep.accept(cause);
  }

  @Override
  public boolean hasNext() {
    beep();
    return cursor.hasNext();
  }

  @Override
  public R next() {
    beep();
    return cursor.next();
  }

  @Override
  public Cursor<R> append(final R element) {
    return Cursor.of(element);
  }
}
