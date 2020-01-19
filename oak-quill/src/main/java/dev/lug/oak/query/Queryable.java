package dev.lug.oak.query;

import dev.lug.oak.func.con.Consumer1;
import dev.lug.oak.func.fun.Function1;
import dev.lug.oak.query.many.Many;
import dev.lug.oak.type.Nullability;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface Queryable<T> extends Iterable<T> {
  default void eventually(final Consumer1<T> consuming) {
    for (final var value : this) consuming.accept(value);
  }
}
