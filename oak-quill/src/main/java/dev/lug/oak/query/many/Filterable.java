package dev.lug.oak.query.many;

import dev.lug.oak.collect.Many;
import dev.lug.oak.func.pre.IntPredicate2;
import dev.lug.oak.func.pre.Predicate1;
import dev.lug.oak.query.Structable;
import dev.lug.oak.type.Nullability;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;

import static java.util.Objects.nonNull;

public interface Filterable<T> extends Structable<T> {
  default Queryable<T> where(final Predicate1<? super T> filter) {
    return new Where<>(this, Nullability.nonNullable(filter, "filter"));
  }

  default Queryable<T> where(final IntPredicate2<? super T> filter) {
    return new WhereIth<>(this, Nullability.nonNullable(filter, "filter"));
  }

  default <C> Queryable<C> ofType(final Class<? extends C> type) {
    return new OfType<>(this, Nullability.nonNullable(type, "type"));
  }
}

final class Where<T> implements Queryable<T> {
  private final Structable<T> structable;
  private final Predicate1<? super T> filter;

  @Contract(pure = true)
  Where(final Structable<T> structable, final Predicate1<? super T> filter) {
    this.structable = structable;
    this.filter = filter;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    final var array = new ArrayList<T>();
    for (final var value : structable) {
      if (filter.test(value))
        array.add(value);
    }
    return array.iterator();
  }
}


final class OfType<T, C> implements Queryable<C> {
  private final Structable<T> structable;
  private final Class<? extends C> type;

  @Contract(pure = true)
  OfType(final Structable<T> structable, final Class<? extends C> type) {
    this.structable = structable;
    this.type = type;
  }

  @NotNull
  @Override
  public final Iterator<C> iterator() {
    final var typeds = new ArrayList<C>();
    for (final var value : structable) {
      if (type.isInstance(value))
        typeds.add(type.cast(value));
    }
    return typeds.iterator();
  }
}

final class WhereIth<T> implements Queryable<T> {
  private final Structable<T> structable;
  private final IntPredicate2<? super T> filter;

  @Contract(pure = true)
  WhereIth(final Structable<T> structable, final IntPredicate2<? super T> filter) {
    this.structable = structable;
    this.filter = filter;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    final var result = new ArrayList<T>();
    var index = 0;
    for (final var cursor = structable.iterator(); cursor.hasNext(); index++) {
      final var value = cursor.next();
      if (nonNull(value) && filter.verify(index, value)) {
        result.add(value);
      }
    }
    return result.iterator();
  }
}
