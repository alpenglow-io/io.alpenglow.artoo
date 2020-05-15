package io.artoor.query.one;

import io.artoor.cursor.Cursor;
import io.artoor.query.One;
import io.artoor.query.Queryable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Predicate;

import static io.artoor.type.Nullability.nonNullable;
import static java.util.function.Function.identity;

public interface Filterable<T extends Record> extends Queryable<T> {
  default One<T> where(final Predicate<? super T> where) {
    return new Where<>(this, nonNullable(where, "where"), identity())::iterator;
  }

  default <R extends Record> One<R> ofType(final Class<R> type) {
    nonNullable(type, "type");
    return new Where<>(this, type::isInstance, type::cast)::iterator;
  }
}

final class Where<T extends Record, R extends Record> implements Filterable<R> {
  private final Queryable<T> queryable;
  private final Predicate<? super T> where;
  private final Function<? super T, ? extends R> select;

  @Contract(pure = true)
  public Where(final Queryable<T> queryable, final Predicate<? super T> where, final Function<? super T, ? extends R> select) {
    this.queryable = queryable;
    this.where = where;
    this.select = select;
  }

  @NotNull
  @Override
  public final Iterator<R> iterator() {
    R result = null;
    for (final var value : queryable) {
      if (value != null && where.test(value)) {
        result = select.apply(value);
      }
    }
    return Cursor.of(result);
  }
}

