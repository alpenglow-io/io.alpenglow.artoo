package io.artoo.lance.next.cursor;

import io.artoo.lance.func.Cons;
import io.artoo.lance.next.Cursor;
import io.artoo.lance.next.Next;

public interface Actionable<T> extends Next<T> {
  default Cursor<T> open() {
    return new Open<>(this);
  }

  default Cursor<T> scroll() {
    return new Scroll<>(this);
  }

  default Cursor<T> scroll(final Cons.Uni<? super Throwable> catch$) {
    return new Scroll<>(this, catch$);
  }

  default Cursor<T> close() {
    return new Close<>(this);
  }
}

final class Scroll<T> implements Cursor<T> {
  private final Scrolled scrolled = new Scrolled();

  private final Next<T> next;
  private final Cons.Uni<? super Throwable> catch$;

  Scroll(final Next<T> next) {
    this(next, it -> {});
  }

  Scroll(final Next<T> next, final Cons.Uni<? super Throwable> catch$) {
    this.next = next;
    this.catch$ = catch$;
  }

  @Override
  public T fetch() {
    if (scrolled.value != null) {
      final var fetched = scrolled.value;
      scrolled.value = null;
      return fetched;

    } else if (scrolled.cause != null) {
      final var thrown = scrolled.cause;
      scrolled.cause = null;
      catch$.accept(thrown);
    }

    return null;
  }

  @Override
  public boolean hasNext() {
    while (scrolled.value == null && scrolled.cause == null && next.hasNext()) {
      try {
        scrolled.value = next.fetch();
      } catch (Throwable throwable) {
        scrolled.cause = throwable;
      }
    }
    return scrolled.value != null || scrolled.cause != null;
  }

  @Override
  public Cursor<T> open() {
    return next instanceof Cursor<T> c ? c.open() : Cursor.nothing();
  }

  private final class Scrolled {
    private T value;
    private Throwable cause;
  }
}

final class Close<T> implements Cursor<T> {
  private final Closed closed = new Closed();

  private final Next<T> next;

  Close(final Next<T> next) {this.next = next;}

  @Override
  public T fetch() throws Throwable {
    if (!hasNext()) return null;

    if (closed.cause != null) {
      final var cause = closed.cause;
      closed.cause = null;
      throw cause;
    }
    final var value = closed.value;
    closed.value = null;
    return value;
  }

  @Override
  public boolean hasNext() {
    while (next.hasNext()) {
      try {
        closed.value = next.fetch();
      } catch (Throwable throwable) {
        closed.cause = throwable;
      }
    }

    return closed.value != null || closed.cause != null;
  }

  private final class Closed {
    private T value;
    private Throwable cause;
  }
}

final class Open<T> implements Cursor<T> {
  private final Next<T> next;

  Open(final Next<T> next) {
    this.next = next;
  }

  @Override
  public T fetch() throws Throwable {
    return next.fetch();
  }

  @Override
  public boolean hasNext() {
    return next.hasNext();
  }
}
