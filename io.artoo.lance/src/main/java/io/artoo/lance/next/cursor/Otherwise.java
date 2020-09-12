package io.artoo.lance.next.cursor;

import io.artoo.lance.func.Func;
import io.artoo.lance.func.Suppl;
import io.artoo.lance.next.Cursor;
import io.artoo.lance.next.Next;

public interface Otherwise<T> extends Actionable<T> {
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

final class Or<T> implements Cursor<T> {
  private final Actual actual = new Actual();

  private final Next<T> left;
  private final Next<T> right;

  Or(final Next<T> left, final Next<T> right) {
    assert left != null && right != null;
    this.left = left;
    this.right = right;
  }

  @Override
  public T fetch() throws Throwable {
    try {
      if (actual.next == null && hasNext() && actual.value != null) {
        return actual.value;
      }

      if (actual.next != null && (actual.value != null || (hasNext() && actual.cause == null))) {
        return actual.value;
      }

      if (actual.next != null && actual.cause != null) {
        throw actual.cause;
      }
    } finally {
      actual.value = null;
      actual.cause = null;
    }

    return null;
  }

  @Override
  public boolean hasNext() {
    try {
      do
      {
        if (actual.next == null && left.hasNext()) {
          actual.next = left;
        } else if (actual.next == null && right.hasNext()) {
          actual.next = right;
        }

        if (actual.value == null && actual.next != null) {
          actual.value = actual.next.fetch();
        }
      } while (actual.value == null && actual.cause == null && (actual.next != null && actual.next.hasNext()));
    } catch (Throwable throwable) {
      actual.cause = throwable;
    }

    return actual.value != null || actual.cause != null;
  }

  private final class Actual {
    public Throwable cause;
    private Next<T> next;
    private T value;
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
