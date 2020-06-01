package io.artoo.lance.query.many;

import io.artoo.lance.query.Many;
import io.artoo.lance.query.Queryable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static io.artoo.lance.type.Nullability.nonNullable;

public interface Partitionable<T extends Record> extends Queryable<T> {
  default Many<T> skip(final int until) {
    return skipWhile((index, it) -> index < until);
  }

  default Many<T> skipWhile(final Predicate<? super T> where) {
    return skipWhile((index, it) -> !where.test(it));
  }

  default Many<T> skipWhile(final BiPredicate<? super Integer, ? super T> where) {
    nonNullable(where, "where");
    return new Partition<>(this, (i, it) -> {}, where.negate())::iterator;
  }

  default Many<T> take(final int until) {
    return new Partition<>(this, (i, it) -> {}, (index, it) -> index < until)::iterator;
  }

  default Many<T> takeWhile(final Predicate<? super T> where) {
    nonNullable(where, "where");
    return takeWhile((index, param) -> where.test(param));
  }

  default Many<T> takeWhile(final BiPredicate<? super Integer, ? super T> where) {
    return new Partition<>(this, (i, it) -> {}, nonNullable(where, "where"))::iterator;
  }
}

final class Partition<T extends Record> implements Partitionable<T> {
  private final Queryable<T> queryable;
  private final BiConsumer<? super Integer, ? super T> peek;
  private final BiPredicate<? super Integer, ? super T> where;

  @Contract(pure = true)
  public Partition(final Queryable<T> queryable, final BiConsumer<? super Integer, ? super T> peek, final BiPredicate<? super Integer, ? super T> where) {
    this.queryable = queryable;
    this.peek = peek;
    this.where = where;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    final var result = new ArrayList<T>();
    var index = 0;
    var cursor = queryable.iterator();
    var verified = false;
    if (cursor.hasNext()) {
      do
      {
        final var it = cursor.next();
        peek.accept(index, it);
        verified = it != null && where.test(index, it);
        if (verified) result.add(it);
        index++;
      } while (cursor.hasNext() && verified);
    }
    return result.iterator();
  }
}

