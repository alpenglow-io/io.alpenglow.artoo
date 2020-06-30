package io.artoo.lance.query.one;

import io.artoo.lance.query.cursor.Cursor;
import io.artoo.lance.func.Func;
import io.artoo.lance.func.Pred;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static io.artoo.lance.type.Nullability.nonNullable;

public interface Filterable<T> extends Queryable<T> {
  default One<T> where(final Pred.Uni<? super T> where) {
    return new Where<>(this, nonNullable(where, "where"), it -> it);
  }

  default <R> One<R> ofType(final Class<R> type) {
    nonNullable(type, "type");
    return new Where<>(this, type::isInstance, type::cast);
  }
}

final class Where<T, R> implements One<R> {
  private final Queryable<T> queryable;
  private final Pred.Uni<? super T> where;
  private final Func.Uni<? super T, ? extends R> select;

  @Contract(pure = true)
  public Where(final Queryable<T> queryable, final Pred.Uni<? super T> where, final Func.Uni<? super T, ? extends R> select) {
    this.queryable = queryable;
    this.where = where;
    this.select = select;
  }

  @NotNull
  @Override
  public final Cursor<R> cursor() {
    R result = null;
    for (final var value : queryable) {
      if (value != null && where.test(value)) {
        result = select.apply(value);
      }
    }
    return Cursor.local(result);
  }
}

