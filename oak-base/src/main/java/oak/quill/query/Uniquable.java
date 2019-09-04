package oak.quill.query;

import oak.collect.Array;
import oak.collect.cursor.Cursor;
import oak.func.pre.Predicate1;
import oak.quill.Structable;
import oak.quill.single.Nullable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static oak.func.pre.Predicate1.tautology;
import static oak.type.Nullability.nonNullable;

public interface Uniquable<T> extends Structable<T> {
  default Nullable<T> elementAt(final int index) {
    return new ElementAt<>(this, index);
  }

  default Nullable<T> first() {
    return new First<>(this, tautology());
  }

  default Nullable<T> first(final Predicate1<? super T> filter) {
    return new First<>(this, nonNullable(filter, "filter"));
  }

  default Nullable<T> last() {
    return new Last<>(this, tautology());
  }

  default Nullable<T> last(final Predicate1<? super T> filter) {
    return new Last<>(this, nonNullable(filter, "filter"));
  }

  default oak.quill.single.Single<T> single() {
    return new Single<>(this, tautology());
  }

  default oak.quill.single.Single<T> single(final Predicate1<? super T> filter) {
    return new Single<>(this, nonNullable(filter, "filter"));
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
    for (var iterator = structable.iterator(); iterator.hasNext() && count < index; count++) {
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
    T returned = null;
    for (var iterator = structable.iterator(); iterator.hasNext() && returned == null; ) {
      final var next = iterator.next();
      if (filter.test(next))
        returned = next;
    }
    return Cursor.ofNullable(returned);
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
    return Cursor.of(last);
  }
}

final class Single<T> implements oak.quill.single.Single<T> {
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
      final var array = Array.<T>of();
      for (final var cursor = structable.iterator(); cursor.hasNext() && array.length() < 2; ) {
        final var next = cursor.next();
        if (filter.test(next)) {
          array.add(next);
        }
      }
      if (array.length() > 1) throw new IllegalStateException("Queryable must satisfy the condition once.");
      return Cursor.of(array.at(0));
    }
  }
}
