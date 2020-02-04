package dev.lug.oak.query.many;

import dev.lug.oak.func.pre.IntPredicate2;
import dev.lug.oak.func.pre.Predicate1;
import dev.lug.oak.query.Many;
import dev.lug.oak.query.Queryable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;

import static dev.lug.oak.type.Nullability.nonNullable;
import static java.util.Objects.nonNull;

public interface Filterable<T> extends Queryable<T> {
  default Many<T> where(final Predicate1<? super T> filter) {
    return new Where<>(this, nonNullable(filter, "filter"));
  }

  default Many<T> where(final IntPredicate2<? super T> filter) {
    return new WhereIth<>(this, nonNullable(filter, "filter"));
  }

  default <C> Many<C> ofType(final Class<? extends C> type) {
    return new OfType<>(this, nonNullable(type, "type"));
  }
}

final class Where<T> implements Many<T> {
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
    final var array = new ArrayList<T>();
    for (final var value : queryable) {
      if (filter.test(value))
        array.add(value);
    }
    return array.iterator();
  }
}


final class OfType<T, C> implements Many<C> {
  private final Queryable<T> queryable;
  private final Class<? extends C> type;

  @Contract(pure = true)
  OfType(final Queryable<T> queryable, final Class<? extends C> type) {
    this.queryable = queryable;
    this.type = type;
  }

  @NotNull
  @Override
  public final Iterator<C> iterator() {
    final var typeds = new ArrayList<C>();
    for (final var value : queryable) {
      if (type.isInstance(value))
        typeds.add(type.cast(value));
    }
    return typeds.iterator();
  }
}

final class WhereIth<T> implements Many<T> {
  private final Queryable<T> queryable;
  private final IntPredicate2<? super T> filter;

  @Contract(pure = true)
  WhereIth(final Queryable<T> queryable, final IntPredicate2<? super T> filter) {
    this.queryable = queryable;
    this.filter = filter;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    final var result = new ArrayList<T>();
    var index = 0;
    for (final var cursor = queryable.iterator(); cursor.hasNext(); index++) {
      final var value = cursor.next();
      if (nonNull(value) && filter.verify(index, value)) {
        result.add(value);
      }
    }
    return result.iterator();
  }
}
