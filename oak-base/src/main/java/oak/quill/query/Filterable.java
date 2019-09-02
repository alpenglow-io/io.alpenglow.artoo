package oak.quill.query;

import oak.collect.Array;
import oak.func.pre.Predicate1;
import oak.quill.Structable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;

import static oak.type.Nullability.nonNullable;

public interface Filterable<T> extends Structable<T> {
  default Queryable<T> where(final Predicate1<? super T> filter) {
    return new Where<>(this, nonNullable(filter, "filter"));
  }

  default <C> Queryable<C> ofType(final Class<? extends C> type) {
    return new OfType<>(this, nonNullable(type, "type"));
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
    final var array = Array.<T>of();
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
    final var typeds = Array.<C>of();
    for (final var value : structable) {
      if (type.isInstance(value))
        typeds.add(type.cast(value));
    }
    return typeds.iterator();
  }
}
