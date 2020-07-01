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
  public final Cursor<T> set(final T... elements) {
    return origin.set(elements);
  }

  @Override
  public Cursor<T> grab(final Throwable cause) {
    return origin.grab(cause);
  }

  @Override
  public Cursor<T> scroll() {
    return origin.scroll();
  }

  @Override
  public boolean has(final T element) {
    return origin.has(element);
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
