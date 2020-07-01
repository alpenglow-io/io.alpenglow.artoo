package io.artoo.lance.query.many;

import io.artoo.lance.query.cursor.Cursor;
import io.artoo.lance.func.Pred;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import org.jetbrains.annotations.NotNull;

import static java.lang.Boolean.TRUE;

interface Countable<T> extends Queryable<T> {
  default One<Integer> count() {
    return this.count(it -> true);
  }

  default One<Integer> count(final Pred.Uni<? super T> where) {
    return new Count<>(this, where);
  }
}

final class Count<T> implements One<Integer> {
  private final Queryable<T> queryable;
  private final Pred.Uni<? super T> where;

  Count(final Queryable<T> queryable, final Pred.Uni<? super T> where) {
    assert queryable != null && where != null;
    this.queryable = queryable;
    this.where = where;
  }

  @NotNull
  @Override
  public final Cursor<Integer> cursor() {
    final var counted = Cursor.local(0);

    final var cursor = queryable.cursor();
    while (cursor.hasNext() && !counted.hasCause()) {
      try {
        if (TRUE.equals(cursor.fetch(where::tryTest))) {
          counted.set(counted.next() + 1);
        }
      } catch (Throwable cause) {
        counted.grab(cause);
      }
    }

    return counted;
  }
}
