package io.artoo.lance.next.cursor;

import io.artoo.lance.func.Func;
import io.artoo.lance.next.Cursor;
import io.artoo.lance.next.Next;

public interface Projectable<T> extends Otherwise<T> {
  default <R> Cursor<R> select(final Func.Uni<? super T, ? extends R> select) {
    return new Select<>(this, select.butNulls());
  }

  default <R> Cursor<R> select(final Func.Bi<? super Integer, ? super T, ? extends R> selectWithIndex) {
    return new Select<>(this, new Index<>(0, selectWithIndex.butNulls()));
  }

  default <R, N extends Next<R>> Cursor<R> selectNext(final Func.Uni<? super T, ? extends N> select) {
    return new SelectNext<R, N>(new Select<>(this, select.butNulls()));
  }

  default <R, N extends Next<R>> Cursor<R> selectNext(final Func.Bi<? super Integer, ? super T, ? extends N> selectWithIndex) {
    return new SelectNext<R, N>(select(selectWithIndex));
  }
}

final class Index<T, R> implements Func.Uni<T, R> {
  private int index;
  private final Func.Bi<? super Integer, ? super T, ? extends R> select;

  Index(final int index, final Func.Bi<? super Integer, ? super T, ? extends R> select) {
    this.index = index;
    this.select = select;
  }

  @Override
  public R tryApply(final T t) throws Throwable {
    return select.tryApply(index++, t);
  }
}

final class Select<T, R> implements Cursor<R> {
  private final Next<T> next;
  private final Func.Uni<? super T, ? extends R> select;

  public Select(final Next<T> next, final Func.Uni<? super T, ? extends R> select) {
    assert next != null && select != null;
    this.next = next;
    this.select = select;
  }

  @Override
  public final <P> Cursor<P> select(final Func.Uni<? super R, ? extends P> selectAgain) {
    return new Select<>(this.next, select.then(selectAgain));
  }

  @Override
  public final R fetch() throws Throwable {
    return select.tryApply(next.fetch());
  }

  @Override
  public final boolean hasNext() {
    return next.hasNext();
  }
}

final class SelectNext<T, N extends Next<T>> implements Cursor<T> {
  private final Flatten flatten = new Flatten();

  private final Next<N> next;

  public SelectNext(final Next<N> next) {this.next = next;}

  @Override
  public T fetch() throws Throwable {
    if (flatten.value != null) {
      final var fetched = flatten.value;
      flatten.value = null;
      return fetched;

    } else if (flatten.cause != null) {
      final var thrown = flatten.cause;
      flatten.cause = null;
      throw thrown;
    }

    return null;
  }

  @Override
  public boolean hasNext() {
    var done = false;
    do
    {
      try {
        if (flatten.next == null)
          if (next.hasNext()) {
            flatten.next = next.fetch();
          } else {
            done = true;
          }

        if (!done && flatten.next.hasNext()) {
          flatten.value = flatten.next.fetch();
        } else {
          flatten.next = null;
        }

      } catch (Throwable throwable) {
        flatten.cause = throwable;
      }
    } while (flatten.value == null && flatten.cause == null && !done);

    return flatten.value != null || flatten.cause != null;
  }

  private final class Flatten {
    private Throwable cause;
    private Next<T> next;
    private T value;
  }
}

