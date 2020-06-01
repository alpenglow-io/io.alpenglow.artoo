package io.artoo.lance.query.many;

import io.artoo.lance.cursor.Cursor;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;


import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import static io.artoo.lance.type.Nullability.nonNullable;

public interface Uniquable<T extends Record> extends Queryable<T> {
  default One<T> at(final int index) {
    return new At<>(this, index)::iterator;
  }

  default One<T> first() {
    return new Unique<>(this, (i, it) -> {}, true, false, it -> true)::iterator;
  }

  default One<T> first(final Predicate<? super T> where) {
    return new Unique<>(this, (i, it) -> {}, true, false, nonNullable(where, "where"))::iterator;
  }

  default One<T> last() {
    return last(it -> true);
  }

  default One<T> last(final Predicate<? super T> where) {
    return new Unique<>(this, (i, it) -> {}, false, false, nonNullable(where, "where"))::iterator;
  }

  default One<T> single() {
    return single(it -> true);
  }

  default One<T> single(final Predicate<? super T> where) {
    return new Unique<>(this, (i, it) -> {}, false, true, nonNullable(where, "where"))::iterator;
  }
}

final class At<T extends Record> implements Uniquable<T> {
  private final Queryable<T> queryable;
  private final int index;

  @Contract(pure = true)
  At(final Queryable<T> queryable, final int index) {
    this.queryable = queryable;
    this.index = index;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    var count = 0;
    T returned = null;
    for (final var iterator = queryable.iterator(); iterator.hasNext() && count <= index; count++) {
      returned = iterator.next();
    }
    if (count < index)
      returned = null;
    return Cursor.of(returned);
  }
}

final class Unique<T extends Record> implements Uniquable<T> {
  private final Queryable<T> queryable;
  private final BiConsumer<? super Integer, ? super T> peek;
  private final boolean first;
  private final boolean single;
  private final Predicate<? super T> where;

  @Contract(pure = true)
  public Unique(final Queryable<T> queryable, final BiConsumer<? super Integer, ? super T> peek, final boolean first, final boolean single, final Predicate<? super T> where) {
    this.queryable = queryable;
    this.peek = peek;
    this.first = first;
    this.single = single;
    this.where = where;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    T result = null;
    var done = false;
    var index = 0;
    for (var cursor = queryable.iterator(); cursor.hasNext() && (!first || result == null) && !done; index++) {
      var it = cursor.next();
      peek.accept(index, it);
      if (it != null && where.test(it)) {
        done = single && result != null;
        result = !single || result == null ? it : null;
      }
    }
    return Cursor.of(result);
  }
}
