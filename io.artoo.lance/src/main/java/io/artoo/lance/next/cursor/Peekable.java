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
  public final Cursor<R> scroll() {
    return scroll(catch$);
  }
}
