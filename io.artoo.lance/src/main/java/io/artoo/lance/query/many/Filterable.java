package io.artoo.lance.query.many;

import io.artoo.lance.query.cursor.Cursor;
import io.artoo.lance.func.Func;
import io.artoo.lance.func.Pred;
import io.artoo.lance.query.Many;
import io.artoo.lance.query.Queryable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static io.artoo.lance.query.many.Index.index;
import static io.artoo.lance.type.Nullability.nonNullable;

public interface Filterable<T> extends Queryable<T> {
  default Many<T> where(final Pred.Uni<? super T> where) {
    nonNullable(where, "where");
    return where((index, param) -> where.test(param));
  }

  default Many<T> where(final Pred.Bi<? super Integer, ? super T> where) {
    return where(where, (i, it) -> it);
  }

  default <R> Many<R> ofType(final Class<? extends R> type) {
    final var t = nonNullable(type, "type");
    return new OfType<>(this, t);
  }

  default <R> Many<R> where(final Pred.Bi<? super Integer, ? super T> where, final Func.Bi<? super Integer, ? super T, ? extends R> select) {
    nonNullable(where, "where");
    nonNullable(select, "select");
    return new Where<T, R>(this, where, select);
  }
}

final class Where<T, R> implements Many<R> {
  private final Queryable<T> queryable;
  private final Pred.Bi<? super Integer, ? super T> where;
  private final Func.Bi<? super Integer, ? super T, ? extends R> select;

  @Contract(pure = true)
  Where(final Queryable<T> queryable, final Pred.Bi<? super Integer, ? super T> where, final Func.Bi<? super Integer, ? super T, ? extends R> select) {
    assert queryable != null && where != null && select != null;
    this.queryable = queryable;
    this.where = where;
    this.select = select;
  }

  @Override
  public final Cursor<R> cursor() {
    final var filtered = Cursor.<R>local();

    final var cursor = queryable.cursor();
    for (var index = index(); cursor.hasNext() && !filtered.hasCause(); index.value++) {
      try {
        filtered.append(
          cursor.<R>next(it -> where.tryTest(index.value, it)
            ? select.tryApply(index.value, it)
            : null
          )
        );
      } catch (Throwable cause) {
        filtered.grab(cause);

      }
    }

    return filtered;
  }
}

final class OfType<T, R> implements Many<R> {
  private final Queryable<T> queryable;
  private final Class<? extends R> type;

  OfType(final Queryable<T> queryable, final Class<? extends R> type) {
    assert queryable != null && type != null;
    this.queryable = queryable;
    this.type = type;
  }

  @NotNull
  @Override
  public final Cursor<R> cursor() {
    final var typed = Cursor.<R>local();

    final var cursor = queryable.cursor();
    while (cursor.hasNext() && !typed.hasCause()) {
      try {
        typed.append(
          cursor.next(it -> type.isInstance(it)
            ? type.cast(it)
            : null
          )
        );
      } catch (Throwable cause) {
        typed.grab(cause);
      }
    }

    return typed;
  }
}

