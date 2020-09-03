package io.artoo.lance.next.cursor;

import io.artoo.lance.func.Func;
import io.artoo.lance.func.Suppl;
import io.artoo.lance.next.Cursor;
import io.artoo.lance.next.Next;

public interface Otherwise<T> extends Next<T> {
  @SuppressWarnings("unchecked")
  default Cursor<T> or(final T... values) {
    return or(Cursor.every(values));
  }

  default Cursor<T> or(final Next<T> otherwise) {
    return new Or<>(this, otherwise);
  }

  default <E extends RuntimeException> Cursor<T> or(final String message, final Func.Uni<? super String, ? extends E> exception) {
    return new Raise<>(this, message, exception);
  }

  default <E extends RuntimeException> Cursor<T> or(final Suppl.Uni<? extends E> exception) {
    return or("nothing", it -> exception.tryGet());
  }
}

@SuppressWarnings("StatementWithEmptyBody")
final class Or<T> implements Cursor<T> {
  private Boolean hasLeft;

  private final Next<T> left;
  private final Next<T> right;

  Or(final Next<T> left, final Next<T> right) {
    this.left = left;
    this.right = right;
  }

  @Override
  public T fetch() throws Throwable {
    T fetched = null;

    try {
      if (hasLeft == null || hasLeft) {
        while (left.hasNext() && (fetched = left.fetch()) == null)
          ;

        if (hasLeft == null) hasLeft = fetched != null;
      }
    } catch (Throwable throwable) {
      hasLeft = false;
    }

    if (!hasLeft) {
      while (right.hasNext() && (fetched = right.fetch()) == null)
        ;
    }

    return fetched;
  }

  @Override
  public boolean hasNext() {
    return (left.hasNext() && (hasLeft == null || hasLeft)) || ((hasLeft == null || !hasLeft) && right.hasNext());
  }
}

@SuppressWarnings("StatementWithEmptyBody")
final class Raise<T, E extends RuntimeException> implements Cursor<T> {
  private Boolean hasLeft;

  private final Next<T> next;
  private final String message;
  private final Func.Uni<? super String, ? extends E> exception;

  Raise(final Next<T> next, final String message, final Func.Uni<? super String, ? extends E> exception) {
    this.next = next;
    this.message = message;
    this.exception = exception;
  }

  @Override
  public T fetch() throws Throwable {
    T fetched = null;

    if (hasLeft == null || hasLeft) {
      while (next.hasNext() && (fetched = next.next()) == null)
        ;

      if (hasLeft == null) hasLeft = fetched != null;
    }

    if (!hasLeft) throw exception.tryApply(message);

    return fetched;
  }

  @Override
  public T next() {
    try {
      return fetch();
    } catch (Throwable throwable) {
      throw exception.apply(message);
    }
  }

  @Override
  public boolean hasNext() {
    return (next.hasNext() && (hasLeft == null || hasLeft));
  }
}
