package io.artoo.lance.query.many;

import io.artoo.lance.cursor.Cursor;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.value.UInt32;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static java.util.function.Function.identity;

interface Countable<T extends Record> extends Queryable<T> {
  default UInt32 count() {
    return this.count(it -> true);
  }

  default UInt32 count(final Predicate<? super T> where) {
    return new Count<>(this, it -> {}, where);
  }
}

interface Postponable<R extends Record> {
  Supplier<R> lately();
}

final class Count<T extends Record> implements One<UInt32>, Postponable<UInt32> {
  private final Queryable<T> queryable;
  private final Consumer<? super T> peek;
  private final Predicate<? super T> where;

  @Contract(pure = true)
  Count(
    final Queryable<T> queryable,
    final Consumer<? super T> peek,
    final Predicate<? super T> where
  ) {
    this.queryable = queryable;
    this.peek = peek;
    this.where = where;
  }

  @NotNull
  @Override
  public final Iterator<UInt32> iterator() {
    var aggregated = UInt32.ZERO;
    for (final var it : queryable) {
      if (peek != null) peek.accept(it);
      if (where.test(it)) {
        aggregated = aggregated.inc();
      }
    }
    return Cursor.of(aggregated);
  }

  @Override
  public Supplier<UInt32> lately() {
    return () -> iterator().next();
  }
}
