package oak.quill.query;

import oak.collect.cursor.Cursor;
import oak.func.pre.Predicate1;
import oak.quill.Structable;
import oak.quill.single.Nullable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static oak.type.Nullability.nonNullable;

public interface Uniquable<T> extends Structable<T> {
  default Nullable<T> at(final int index) {
    return new At<>(this, index);
  }

  default Nullable<T> first() {
    return new First<>(this, it -> true);
  }

  default Nullable<T> first(final Predicate1<? super T> filter) {
    return new First<>(this, nonNullable(filter, "filter"));
  }

  default Nullable<T> last() {
    return new Last<>(this, it -> true);
  }

  default Nullable<T> last(final Predicate1<? super T> filter) {
    return new Last<>(this, nonNullable(filter, "filter"));
  }

  default oak.quill.single.Single<T> single() {
    return new Single<>(this);
  }
}

final class At<T> implements Nullable<T> {
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

  @Contract(pure = true)
  Single(final Structable<T> structable) {this.structable = structable;}

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    final var cursor = structable.iterator();
    final var returned = cursor.next();
    if (cursor.hasNext())
      throw new IllegalStateException("Queryable must contain one element.");
    return Cursor.of(returned);
  }
}
