package dev.lug.oak.query.one;

import dev.lug.oak.collect.cursor.Cursor;
import dev.lug.oak.func.pre.Predicate1;
import dev.lug.oak.query.Queryable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static dev.lug.oak.type.Nullability.nonNullable;

public interface Filterable<T> extends Queryable<T> {
  default One<T> where(Predicate1<? super T> filter) { return new Where<>(this, nonNullable(filter, "filter")); }

  default <R> One<R> ofType(Class<? extends R> type) { return new OfType<>(this, nonNullable(type, "type")); }
}

final class Where<T> implements One<T> {
  private final Queryable<T> queryable;
  private final Predicate1<? super T> filter;

  @Contract(pure = true)
  Where(final Queryable<T> queryable, final Predicate1<? super T> filter) {
    this.queryable = queryable;
    this.filter = filter;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    final var value = queryable.iterator().next();
    return Cursor.ofNullable(value != null && filter.test(value) ? value : null);
  }
}

final class OfType<T, R> implements One<R> {
  private final Queryable<T> queryable;
  private final Class<? extends R> type;

  @Contract(pure = true)
  OfType(final Queryable<T> queryable, final Class<? extends R> type) {
    this.queryable = queryable;
    this.type = type;
  }

  @NotNull
  @Override
  public final Iterator<R> iterator() {
    final var value = queryable.iterator().next();
    return Cursor.ofNullable(value != null && value.getClass().equals(type) ? type.cast(value) : null);
  }
}
