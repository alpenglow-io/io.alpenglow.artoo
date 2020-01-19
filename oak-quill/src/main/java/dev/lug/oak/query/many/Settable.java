package dev.lug.oak.query.many;

import dev.lug.oak.func.pre.Predicate1;
import dev.lug.oak.query.Queryable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;

import static dev.lug.oak.func.pre.Predicate1.tautology;
import static dev.lug.oak.type.Nullability.nonNullable;
import static java.util.Objects.nonNull;

public interface Settable<T> extends Queryable<T> {
  default Many<T> distinct() {
    return new Distinct<>(this);
  }

  default Many<T> distinct(final Predicate1<? super T> filter) {
    return new Distinct<>(this, nonNullable(filter, "filter"));
  }
}

final class Distinct<T> implements Many<T> {
  private final Queryable<T> queryable;
  private final Predicate1<? super T> filter;

  @Contract(pure = true)
  Distinct(final Queryable<T> queryable) {
    this(
      queryable,
      tautology()
    );
  }
  @Contract(pure = true)
  Distinct(final Queryable<T> queryable, final Predicate1<? super T> filter) {
    this.queryable = queryable;
    this.filter = filter;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    final var result = new ArrayList<T>();
    for (final var value : queryable) {
      if (nonNull(value) && filter.test(value) && !result.contains(value) || !filter.test(value))
        result.add(value);
    }
    return result.iterator();
  }
}
