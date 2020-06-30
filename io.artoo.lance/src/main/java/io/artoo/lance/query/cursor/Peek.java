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
  public final Cursor<R> next(final R... elements) {
    return origin.next(elements);
  }

  @Override
  public Cursor<R> cause(final Throwable cause) {
    return origin.cause(cause);
  }
}
