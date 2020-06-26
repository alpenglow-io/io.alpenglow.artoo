package io.artoo.lance.query.cursor;

import io.artoo.lance.func.Cons;

final class Peek<R> implements Cursor<R> {
  private final Cursor<R> cursor;
  private final Cons.Uni<? super R> peek;

  Peek(final Cursor<R> cursor, final Cons.Uni<? super R> peek) {
    assert cursor != null && peek != null;
    this.cursor = cursor;
    this.peek = peek;
  }

  @Override
  public boolean hasNext() {
    return cursor.hasNext();
  }

  @Override
  public R next() {
    final var next = cursor.next();
    if (next != null) {
      try {
        peek.tryApply(next);
      } catch (Throwable throwable) {
        throwable.printStackTrace();
      }
    }
    return next;
  }

  @Override
  public Cursor<R> append(final R element) {
    return cursor.append(element);
  }
}
