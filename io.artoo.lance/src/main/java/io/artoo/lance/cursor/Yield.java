package io.artoo.lance.cursor;

import io.artoo.lance.func.Cons;

final class Yield<T> implements Cursor<T> {
  private final Cursor<T> cursor;
  private final Cons.Uni<? super Throwable> catch$;
  private final Shrunk shrunk;

  Yield(final Cursor<T> cursor) {
    this(cursor, it -> {});
  }
  Yield(final Cursor<T> cursor, final Cons.Uni<? super Throwable> catch$) {
    this.cursor = cursor;
    this.catch$ = catch$;
    this.shrunk = new Shrunk();
  }

  @Override
  public T fetch() {
    return next();
  }

  @Override
  public boolean hasNext() {
    try {
      var hasNext = cursor.hasNext();
      shrunk.fetched = null;
      while (hasNext && shrunk.fetched == null) {
        shrunk.fetched = cursor.next();
        hasNext = shrunk.fetched != null || cursor.hasNext();
      }
      return hasNext;
    } catch (Throwable throwable) {
      catch$.accept(throwable);
      return false;
    }
  }

  @Override
  public T next() {
    return shrunk.fetched();
  }

  private final class Shrunk {
    private T fetched;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private T fetched() {
      if (fetched == null) hasNext();
      return fetched;
    }
  }
}
