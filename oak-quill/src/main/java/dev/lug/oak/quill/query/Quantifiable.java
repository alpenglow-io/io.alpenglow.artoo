package dev.lug.oak.quill.query;

import dev.lug.oak.collect.cursor.Cursor;
import dev.lug.oak.func.pre.Predicate1;
import dev.lug.oak.quill.Structable;
import dev.lug.oak.quill.single.One;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static dev.lug.oak.type.Nullability.nonNullable;
import static java.util.Objects.nonNull;

public interface Quantifiable<T> extends Structable<T> {
  default <C> Queryable<T> allOf(final Class<C> type) {
    return new AllOf<>(this, type);
  }

  default One<Boolean> all(final Predicate1<T> filter) {
    return new All<>(this, nonNullable(filter, "filter"));
  }

  default One<Boolean> any() { return this.any(it -> true); }

  default One<Boolean> any(final Predicate1<? super T> filter) {
    return new Any<>(this, nonNullable(filter, "filter"));
  }
}

final class AllOf<T, C> implements Queryable<T> {
  private final Structable<T> structable;
  private final Class<C> type;

  @Contract(pure = true)
  AllOf(final Structable<T> structable, final Class<C> type) {
    this.structable = structable;
    this.type = type;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    var all = true;
    final var cursor = structable.iterator();
    while (cursor.hasNext() && all) {
      all = type.isInstance(cursor.next());
    }
    return all ? structable.iterator() : Cursor.none();
  }
}

final class All<T> implements One<Boolean> {
  private final Structable<T> structable;
  private final Predicate1<? super T> filter;

  @Contract(pure = true)
  All(final Structable<T> structable, final Predicate1<? super T> filter) {
    this.structable = structable;
    this.filter = filter;
  }

  @NotNull
  @Override
  public final Iterator<Boolean> iterator() {
    var all = true;
    for (final var iterator = structable.iterator(); iterator.hasNext() && all;) {
      final var next = iterator.next();
      all = nonNull(next) && filter.test(next);
    }
    return Cursor.of(all);
  }
}

final class Any<T> implements One<Boolean> {
  private final Structable<T> structable;
  private final Predicate1<? super T> filter;

  @Contract(pure = true)
  Any(final Structable<T> structable, final Predicate1<? super T> filter) {
    this.structable = structable;
    this.filter = filter;
  }

  @NotNull
  @Contract(pure = true)
  @Override
  public final Iterator<Boolean> iterator() {
    var any = false;
    for (final var iterator = structable.iterator(); iterator.hasNext() && !any;) {
      final var next = iterator.next();
      any = nonNull(next) && filter.test(next);
    }
    return Cursor.of(any);
  }
}
