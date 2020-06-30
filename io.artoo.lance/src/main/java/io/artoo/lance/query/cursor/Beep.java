package io.artoo.lance.query.cursor;

import io.artoo.lance.func.Cons;

final class Beep<T> implements Cursor<T> {
  private final Cursor<T> origin;
  private final Cons.Uni<? super Throwable> beep;

  Beep(final Cursor<T> origin, final Cons.Uni<? super Throwable> beep) {
    this.origin = origin;
    this.beep = beep;
  }

  @Override
  public Cursor<T> append(final T element) {
    return origin.append(element);
  }

  @SafeVarargs
  @Override
  public final Cursor<T> next(final T... elements) {
    return origin.next(elements);
  }

  @Override
  public Cursor<T> cause(final Throwable cause) {
    return origin.cause(cause);
  }

  @Override
  public boolean hasNext() {
    if (origin.hasCause()) beep.accept(origin.cause());
    return origin.hasNext();
  }

  @Override
  public T next() {
    return origin.next();
  }
}
