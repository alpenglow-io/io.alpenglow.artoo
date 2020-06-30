package io.artoo.lance.query.many;

import io.artoo.lance.query.cursor.Cursor;
import io.artoo.lance.func.Cons;
import io.artoo.lance.func.Func;
import io.artoo.lance.func.Pred;
import io.artoo.lance.query.Many;
import io.artoo.lance.query.Queryable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

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
    return new OfType<>(this, (i, it) -> {}, t);
  }

  default <R> Many<R> where(final Pred.Bi<? super Integer, ? super T> where, final Func.Bi<? super Integer, ? super T, ? extends R> select) {
    nonNullable(where, "where");
    nonNullable(select, "select");
    return new Where<T, R>(this, (i, it) -> {}, where, select);
  }
}

final class Where<T, R> implements Many<R> {
  private final Queryable<T> queryable;
  private final Cons.Bi<? super Integer, ? super T> peek;
  private final Pred.Bi<? super Integer, ? super T> where;
  private final Func.Bi<? super Integer, ? super T, ? extends R> select;

  @Contract(pure = true)
  Where(final Queryable<T> queryable, final Cons.Bi<? super Integer, ? super T> peek, final Pred.Bi<? super Integer, ? super T> where, final Func.Bi<? super Integer, ? super T, ? extends R> select) {
    this.queryable = queryable;
    this.peek = peek;
    this.where = where;
    this.select = select;
  }

  @NotNull
  @Override
  public final Cursor<R> cursor() {
    final var result = new ArrayList<R>();
    var index = 0;
    for (final var cursor = queryable.iterator(); cursor.hasNext(); index++) {
      final var it = cursor.next();
      peek.accept(index, it);
      if (it != null && where.test(index, it)) {
        result.add(select.apply(index, it));
      }
    }
    final var iterator = result.iterator();
    return new Cursor<R>() {
      @Override
      public Cursor<R> append(final R element) {
        return null;
      }

      @Override
      public Cursor<R> next(final R... elements) {
        return null;
      }

      @Override
      public Cursor<R> cause(final Throwable cause) {
        return null;
      }

      @Override
      public boolean hasNext() {
        return iterator.hasNext();
      }

      @Override
      public R next() {
        return iterator.next();
      }
    };
  }
}

final class OfType<T, R> implements Many<R> {
  private final Queryable<T> queryable;
  private final Cons.Bi<? super Integer, ? super T> peek;
  private final Class<? extends R> type;

  OfType(final Queryable<T> queryable, final Cons.Bi<? super Integer, ? super T> peek, final Class<? extends R> type) {
    this.queryable = queryable;
    this.peek = peek;
    this.type = type;
  }

  @NotNull
  @Override
  public final Cursor<R> cursor() {
    final var records = new ArrayList<R>();
    for (final var record : queryable) {
      if (type.isInstance(record)) {
        records.add(type.cast(record));
      }
    }
    return Cursor.from(records.iterator());
  }
}

