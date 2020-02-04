package dev.lug.oak.query.many;

import dev.lug.oak.func.pre.LongPredicate2;
import dev.lug.oak.func.pre.Predicate1;
import dev.lug.oak.query.Many;
import dev.lug.oak.query.Queryable;
import dev.lug.oak.type.Nullability;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;

public interface Partitionable<T> extends Queryable<T> {
  default Many<T> skip(final int until) {
    return new Skip<>(this, until);
  }
  default Many<T> skipWhile(final Predicate1<? super T> filter) {
    final var expression = Nullability.nonNullable(filter, "filter");
    return new SkipWhile<>(this, (index, param) -> expression.test(param));
  }
  default Many<T> skipWhile(final LongPredicate2<? super T> filter) {
    final var expression = Nullability.nonNullable(filter, "filter");
    return new SkipWhile<>(this, expression);
  }
  default Many<T> take(final int until) {
    return new Take<>(this, until);
  }
  default Many<T> takeWhile(final Predicate1<? super T> filter) {
    final var expression = Nullability.nonNullable(filter, "filter");
    return new TakeWhile<>(this, (index, param) -> expression.test(param));
  }
  default Many<T> takeWhile(final LongPredicate2<? super T> filter) {
    return new TakeWhile<>(this, Nullability.nonNullable(filter, "filter"));
  }
}

final class Skip<S> implements Many<S> {
  private final Queryable<S> queryable;
  private final int until;

  @Contract(pure = true)
  Skip(final Queryable<S> queryable, final int until) {
    this.queryable = queryable;
    this.until = until;
  }

  @NotNull
  @Override
  public final Iterator<S> iterator() {
    var skip = 0;
    var seq = new ArrayList<S>();
    for (final var it : queryable) if (skip++ >= until) seq.add(it);
    return seq.iterator();
  }
}

final class SkipWhile<S> implements Many<S> {
  private final Queryable<S> queryable;
  private final LongPredicate2<? super S> filter;

  @Contract(pure = true)
  SkipWhile(final Queryable<S> queryable, final LongPredicate2<? super S> filter) {
    this.queryable = queryable;
    this.filter = filter;
  }

  @NotNull
  @Override
  public final Iterator<S> iterator() {
    final var result = new ArrayList<S>();
    var keepSkipping = true;
    var index = 0L;
    for (final var cursor = queryable.iterator(); cursor.hasNext(); index++) {
      var value = cursor.next();
      if (!filter.verify(index, value) || !keepSkipping) {
        result.add(value);
        keepSkipping = false;
      }
    }
    return result.iterator();
  }
}

final class Take<S> implements Many<S> {
  private final Queryable<S> source;
  private final int until;

  @Contract(pure = true)
  Take(final Queryable<S> source, final int until) {
    this.source = source;
    this.until = until;
  }

  @NotNull
  @Override
  public final Iterator<S> iterator() {
    var take = 0;
    var seq = new ArrayList<S>();
    for (final var it : source) if (take++ < until) seq.add(it);
    return seq.iterator();
  }
}

final class TakeWhile<S> implements Many<S> {
  private final Queryable<S> some;
  private final LongPredicate2<? super S> expression;

  @Contract(pure = true)
  TakeWhile(final Queryable<S> some, final LongPredicate2<? super S> expression) {
    this.some = some;
    this.expression = expression;
  }

  @NotNull
  @Override
  public final Iterator<S> iterator() {
    final var result = new ArrayList<S>();
    var keepTaking = true;
    var index = 0L;
    for (var cursor = some.iterator(); cursor.hasNext() && keepTaking; index++) {
      final var it = cursor.next();
      if (expression.verify(index, it)) {
        result.add(it);
      } else {
        keepTaking = false;
      }
    }
    return result.iterator();
  }
}
