package dev.lug.oak.quill.query;

import dev.lug.oak.collect.Many;
import dev.lug.oak.type.Nullability;
import dev.lug.oak.quill.Structable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public interface Concatenatable<T> extends Structable<T> {
  default <S extends Structable<T>> Queryable<T> concat(final S structable) {
    return new Concat<>(this, Nullability.nonNullable(structable, "structable"));
  }
}

final class Concat<T, S extends Structable<T>> implements Queryable<T> {
  private final Structable<T> first;
  private final S second;

  @Contract(pure = true)
  Concat(final Structable<T> first, final S second) {
    this.first = first;
    this.second = second;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    final var array = Many.<T>empty();
    for (final var value : first) array.add(value);
    for (final var value : second) array.add(value);
    return array.iterator();
  }
}
