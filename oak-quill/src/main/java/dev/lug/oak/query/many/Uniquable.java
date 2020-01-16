package dev.lug.oak.query.many;

import dev.lug.oak.collect.cursor.Cursor;
import dev.lug.oak.func.pre.Predicate1;
import dev.lug.oak.query.Structable;
import dev.lug.oak.query.one.One;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;

import static dev.lug.oak.func.pre.Predicate1.tautology;
import static dev.lug.oak.type.Nullability.nonNullable;

public interface Uniquable<T> extends Structable<T> {
  default One<T> at(final int index) {
    return new At<>(this, index);
  }

  default One<T> first() {
    return new First<>(this, tautology());
  }

  default One<T> first(final Predicate1<? super T> filter) {
    return new First<>(this, nonNullable(filter, "filter"));
  }

  default One<T> last() {
    return new Last<>(this, tautology());
  }

  default One<T> last(final Predicate1<? super T> filter) {
    return new Last<>(this, nonNullable(filter, "filter"));
  }

  default One<T> single() {
    return new Single<>(this, tautology());
  }

  default One<T> single(final Predicate1<? super T> filter) {
    return new Single<>(this, nonNullable(filter, "filter"));
  }
}

final class At<T> implements One<T> {
  private final Structable<T> structable;
  private final int index;

  @Contract(pure = true)
  At(final Structable<T> structable, final int index) {
    this.structable = structable;
    this.index = index;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    var count = 0;
    T returned = null;
    for (final var iterator = structable.iterator(); iterator.hasNext() && count <= index; count++) {
      returned = iterator.next();
    }
    if (count < index)
      returned = null;
    return Cursor.ofNullable(returned);
  }
}

final class First<T> implements One<T> {
  private final Structable<T> structable;
  private final Predicate1<? super T> filter;

  @Contract(pure = true)
  First(final Structable<T> structable, final Predicate1<? super T> filter) {
    this.structable = structable;
    this.filter = filter;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    T result = null;
    final var cursor = structable.iterator();
    if (cursor.hasNext()) result = cursor.next();
    if (cursor.hasNext()) result = null;
    return Cursor.ofNullable(result);
  }
}

final class Last<T> implements One<T> {
  private final Structable<T> structable;
  private final Predicate1<? super T> filter;

  @Contract(pure = true)
  Last(final Structable<T> structable, final Predicate1<? super T> filter) {
    this.structable = structable;
    this.filter = filter;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    T last = null;
    for (final var value : structable) {
      if (filter.test(value))
        last = value;
    }
    return Cursor.ofNullable(last);
  }
}

final class Single<T> implements One<T> {
  private final Structable<T> structable;
  private final Predicate1<? super T> filter;

  @Contract(pure = true)
  Single(final Structable<T> structable, final Predicate1<? super T> filter) {
    this.structable = structable;
    this.filter = filter;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    if (filter.equals(tautology())) {
      final var cursor = structable.iterator();
      final var returned = cursor.next();
      if (cursor.hasNext())
        throw new IllegalStateException("Queryable must contain one element.");
      return Cursor.of(returned);
    } else {
      final var array = new ArrayList<T>();
      for (final var cursor = structable.iterator(); cursor.hasNext() && array.size() < 2; ) {
        final var next = cursor.next();
        if (filter.test(next)) {
          array.add(next);
        }
      }
      if (array.size() > 1)
        throw new IllegalStateException("Queryable must satisfy the condition once.");
      return Cursor.of(array.get(0));
    }
  }
}
