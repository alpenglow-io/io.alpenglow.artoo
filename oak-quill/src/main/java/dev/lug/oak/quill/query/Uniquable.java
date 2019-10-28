package dev.lug.oak.quill.query;

import dev.lug.oak.collect.Many;
import dev.lug.oak.collect.cursor.Cursor;
import dev.lug.oak.type.Nullability;
import dev.lug.oak.func.pre.Predicate1;
import dev.lug.oak.quill.Structable;
import dev.lug.oak.quill.single.Nullable;
import dev.lug.oak.quill.single.Single;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static dev.lug.oak.func.pre.Predicate1.tautology;

public interface Uniquable<T> extends Structable<T> {
  default Nullable<T> elementAt(final int index) {
    return new ElementAt<>(this, index);
  }

  default Nullable<T> first() {
    return new First<>(this, tautology());
  }

  default Nullable<T> first(final Predicate1<? super T> filter) {
    return new First<>(this, Nullability.nonNullable(filter, "filter"));
  }

  default Nullable<T> last() {
    return new Last<>(this, tautology());
  }

  default Nullable<T> last(final Predicate1<? super T> filter) {
    return new Last<>(this, Nullability.nonNullable(filter, "filter"));
  }

  default Single<T> single() {
    return new One<>(this, tautology());
  }

  default Single<T> single(final Predicate1<? super T> filter) {
    return new One<>(this, Nullability.nonNullable(filter, "filter"));
  }
}

final class ElementAt<T> implements Nullable<T> {
  private final Structable<T> structable;
  private final int index;

  @Contract(pure = true)
  ElementAt(final Structable<T> structable, final int index) {
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

final class First<T> implements Nullable<T> {
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

final class Last<T> implements Nullable<T> {
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

final class One<T> implements Single<T> {
  private final Structable<T> structable;
  private final Predicate1<? super T> filter;

  @Contract(pure = true)
  One(final Structable<T> structable, final Predicate1<? super T> filter) {
    this.structable = structable;
    this.filter = filter;
  }

  @Override
  public final T get() {
    if (filter.equals(tautology())) {
      final var cursor = structable.iterator();
      final var returned = cursor.next();
      if (cursor.hasNext())
        throw new IllegalStateException("Queryable must contain one element.");
      return returned;
    } else {
      final var array = Many.<T>of();
      for (final var cursor = structable.iterator(); cursor.hasNext() && array.length() < 2; ) {
        final var next = cursor.next();
        if (filter.test(next)) {
          array.add(next);
        }
      }
      if (array.length() > 1)
        throw new IllegalStateException("Queryable must satisfy the condition once.");
      return array.at(0);
    }
  }
}
