package dev.lug.oak.query.many;

import dev.lug.oak.cursor.Cursor;
import dev.lug.oak.func.Pre;
import dev.lug.oak.query.Many;
import dev.lug.oak.query.Queryable;
import dev.lug.oak.query.one.One;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static dev.lug.oak.type.Nullability.nonNullable;
import static java.util.Objects.nonNull;

public interface Quantifiable<T> extends Queryable<T> {
  default <C> Many<T> allOf(final Class<C> type) {
    return new AllOf<>(this, type);
  }

  default One<Boolean> all(final Pre<T> filter) {
    return new All<>(this, nonNullable(filter, "filter"));
  }

  default One<Boolean> any() { return this.any(it -> true); }

  default One<Boolean> any(final Pre<? super T> filter) {
    return new Any<>(this, nonNullable(filter, "filter"));
  }
}

final class AllOf<T, C> implements Many<T> {
  private final Queryable<T> queryable;
  private final Class<C> type;

  @Contract(pure = true)
  AllOf(final Queryable<T> queryable, final Class<C> type) {
    this.queryable = queryable;
    this.type = type;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    var all = true;
    final var cursor = queryable.iterator();
    while (cursor.hasNext() && all) {
      all = type.isInstance(cursor.next());
    }
    return all ? queryable.iterator() : Cursor.none();
  }
}

final class All<T> implements One<Boolean> {
  private final Queryable<T> queryable;
  private final Pre<? super T> filter;

  @Contract(pure = true)
  All(final Queryable<T> queryable, final Pre<? super T> filter) {
    this.queryable = queryable;
    this.filter = filter;
  }

  @NotNull
  @Override
  public final Iterator<Boolean> iterator() {
    var all = true;
    for (final var iterator = queryable.iterator(); iterator.hasNext() && all;) {
      final var next = iterator.next();
      all = nonNull(next) && filter.test(next);
    }
    return Cursor.once(all);
  }
}

final class Any<T> implements One<Boolean> {
  private final Queryable<T> queryable;
  private final Pre<? super T> filter;

  @Contract(pure = true)
  Any(final Queryable<T> queryable, final Pre<? super T> filter) {
    this.queryable = queryable;
    this.filter = filter;
  }

  @NotNull
  @Contract(pure = true)
  @Override
  public final Iterator<Boolean> iterator() {
    var any = false;
    for (final var iterator = queryable.iterator(); iterator.hasNext() && !any;) {
      final var next = iterator.next();
      any = nonNull(next) && filter.test(next);
    }
    return Cursor.once(any);
  }
}
