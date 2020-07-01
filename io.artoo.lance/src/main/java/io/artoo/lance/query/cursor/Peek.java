package io.artoo.lance.query.cursor;

import io.artoo.lance.func.Cons;

final class Peek<R> implements Cursor<R> {
  private final Cursor<R> origin;
  private final Cons.Uni<? super R> peek;

  Peek(final Cursor<R> origin, final Cons.Uni<? super R> peek) {
    assert origin != null && peek != null;
    this.origin = origin;
    this.peek = peek;
  }

  @Override
  public boolean hasNext() {
    return origin.hasNext();
  }

  @Override
  public R next() {
    final var next = origin.next();
    if (next != null) {
      try {
        peek.tryAccept(next);
      } catch (Throwable cause) {
        cause.printStackTrace();
      }
    }
    return next;
  }

  @Override
  public Cursor<R> append(final R element) {
    return origin.append(element);
  }

  @SafeVarargs
  @Override
  public final Cursor<R> set(final R... elements) {
    return origin.set(elements);
  }

  @Override
  public Cursor<R> grab(final Throwable cause) {
    return origin.grab(cause);
  }

  @Override
  public Cursor<R> scroll() {
    return origin.scroll();
  }
}
