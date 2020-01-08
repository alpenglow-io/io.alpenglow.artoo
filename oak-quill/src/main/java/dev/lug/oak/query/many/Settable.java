package dev.lug.oak.query.many;

import dev.lug.oak.collect.Many;
import dev.lug.oak.func.pre.Predicate1;
import dev.lug.oak.query.Structable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static dev.lug.oak.func.pre.Predicate1.tautology;
import static dev.lug.oak.type.Nullability.nonNullable;
import static java.util.Objects.nonNull;

public interface Settable<T> extends Structable<T> {
  default Queryable<T> distinct() {
    return new Distinct<>(this);
  }

  default Queryable<T> distinct(final Predicate1<? super T> filter) {
    return new Distinct<>(this, nonNullable(filter, "filter"));
  }
}

final class Distinct<T> implements Queryable<T> {
  private final Structable<T> structable;
  private final Predicate1<? super T> filter;

  @Contract(pure = true)
  Distinct(final Structable<T> structable) {
    this(
      structable,
      tautology()
    );
  }
  @Contract(pure = true)
  Distinct(final Structable<T> structable, final Predicate1<? super T> filter) {
    this.structable = structable;
    this.filter = filter;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    final var result = Many.<T>of();
    for (final var value : structable) {
      if (nonNull(value) && filter.test(value) && result.hasNot(value) || !filter.test(value))
        result.add(value);
    }
    return result.iterator();
  }
}
