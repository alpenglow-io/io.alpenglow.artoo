package dev.lug.oak.query.many;

import dev.lug.oak.query.Queryable;
import dev.lug.oak.type.Nullability;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;

public interface Concatenatable<T> extends Queryable<T> {
  default <S extends Queryable<T>> Many<T> concat(final S structable) {
    return new Concat<>(this, Nullability.nonNullable(structable, "structable"));
  }
}

final class Concat<T, S extends Queryable<T>> implements Many<T> {
  private final Queryable<T> first;
  private final S second;

  @Contract(pure = true)
  Concat(final Queryable<T> first, final S second) {
    this.first = first;
    this.second = second;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    final var array = new ArrayList<T>();
    for (final var value : first) array.add(value);
    for (final var value : second) array.add(value);
    return array.iterator();
  }
}
