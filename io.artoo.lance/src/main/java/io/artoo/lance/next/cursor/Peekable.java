package io.artoo.lance.next.cursor;

import io.artoo.lance.func.Cons;
import io.artoo.lance.next.Cursor;
import io.artoo.lance.next.Next;

public interface Peekable<T> extends Projectable<T> {
  default Cursor<T> peek(Cons.Uni<? super T> peek) {
    return select(it -> { peek.tryAccept(it); return it; });
  }

  default Cursor<T> peek(Cons.Bi<? super Integer, ? super T> peek) {
    return select((index, it) -> { peek.tryAccept(index, it); return it; });
  }

  default Cursor<T> exceptionally(Cons.Uni<? super Throwable> catch$) {
    return new Exceptionally<>(this, catch$);
  }

  default Cursor<T> yield() {
    return new Yield<>(this);
  }
}

final class Exceptionally<R> implements Cursor<R> {
  private final Next<R> next;
  private final Cons.Uni<? super Throwable> catch$;

  Exceptionally(final Next<R> next, final Cons.Uni<? super Throwable> catch$) {
    assert catch$ != null;
    this.next = next;
    this.catch$ = catch$;
  }

  @Override
  public R fetch() {
    try {
      return next.fetch();
    } catch (Throwable throwable) {
      catch$.accept(throwable);
      return null;
    }
  }

  @Override
  public boolean hasNext() {
    return next.hasNext();
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

  @Override
  public Cursor<R> yield() {
    return new Yield<>(next, catch$);
  }
}

final class Yield<T> implements Cursor<T> {
  private final Shrunk shrunk = new Shrunk();
  private final Next<T> next;
  private final Cons.Uni<? super Throwable> catch$;

  Yield(final Next<T> next) {
    this(next, it -> {});
  }

  Yield(final Next<T> next, final Cons.Uni<? super Throwable> catch$) {
    this.next = next;
    this.catch$ = catch$;
  }

  @Override
  public T fetch() {
    return next();
  }

  @Override
  public boolean hasNext() {
    try {
      var hasNext = next.hasNext();
      shrunk.fetched = null;
      while (hasNext && shrunk.fetched == null) {
        shrunk.fetched = next.next();
        hasNext = shrunk.fetched != null || next.hasNext();
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
      if (fetched == null)
        hasNext();
      return fetched;
    }
  }
}
