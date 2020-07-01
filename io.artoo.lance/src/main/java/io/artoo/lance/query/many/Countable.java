package io.artoo.lance.query.many;

import io.artoo.lance.query.cursor.Cursor;
import io.artoo.lance.func.Cons;
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
    return new Count<>(this, it -> {}, where);
  }
}

final class Count<T> implements One<Integer> {
  private final Queryable<T> queryable;
  private final Cons.Uni<? super T> peek;
  private final Pred.Uni<? super T> where;

  Count(final Queryable<T> queryable, final Cons.Uni<? super T> peek, final Pred.Uni<? super T> where) {
    assert queryable != null && peek != null && where != null;
    this.queryable = queryable;
    this.peek = peek;
    this.where = where;
  }

  @NotNull
  @Override
  public final Cursor<Integer> cursor() {
    final var counted = Cursor.local(0);

    final var cursor = queryable.cursor();
    while (cursor.hasNext() && !counted.hasCause()) {
      try {
        if (TRUE.equals(cursor.next(where::tryTest))) {
          counted.next(counted.next() + 1);
        }
      } catch (Throwable cause) {
        counted.cause(cause);
      }
    }

    return counted;
  }
}
