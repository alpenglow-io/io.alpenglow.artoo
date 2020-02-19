package oak.query.many;

import oak.func.$2.IntPre;
import oak.func.Pre;
import oak.query.Many;
import oak.query.Queryable;
import oak.type.Nullability;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;

import static oak.type.Nullability.nonNullable;
import static java.util.Objects.nonNull;

public interface Filterable<T> extends Queryable<T> {
  default Many<T> where(final Pre<? super T> filter) {
    return new Where<>(this, Nullability.nonNullable(filter, "filter"));
  }

  default Many<T> where(final IntPre<? super T> filter) {
    return new WhereIth<>(this, Nullability.nonNullable(filter, "filter"));
  }

  default <C> Many<C> ofType(final Class<? extends C> type) {
    return new OfType<>(this, Nullability.nonNullable(type, "type"));
  }
}

final class Where<T> implements Many<T> {
  private final Queryable<T> queryable;
  private final Pre<? super T> filter;

  @Contract(pure = true)
  Where(final Queryable<T> queryable, final Pre<? super T> filter) {
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
  private final IntPre<? super T> filter;

  @Contract(pure = true)
  WhereIth(final Queryable<T> queryable, final IntPre<? super T> filter) {
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
