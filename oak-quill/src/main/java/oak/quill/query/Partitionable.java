package oak.quill.query;

import oak.collect.Many;
import oak.func.pre.Predicate1;
import oak.func.pre.LongPredicate2;
import oak.quill.Structable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;

import static oak.type.Nullability.nonNullable;

public interface Partitionable<T> extends Structable<T> {
  default Queryable<T> skip(final int until) {
    return new Skip<>(this, until);
  }
  default Queryable<T> skipWhile(final Predicate1<? super T> filter) {
    final var expression = nonNullable(filter, "filter");
    return new SkipWhile<>(this, (index, param) -> expression.test(param));
  }
  default Queryable<T> skipWhile(final LongPredicate2<? super T> filter) {
    final var expression = nonNullable(filter, "filter");
    return new SkipWhile<>(this, expression);
  }
  default Queryable<T> take(final int until) {
    return new Take<>(this, until);
  }
  default Queryable<T> takeWhile(final Predicate1<? super T> filter) {
    final var expression = nonNullable(filter, "filter");
    return new TakeWhile<>(this, (index, param) -> expression.test(param));
  }
  default Queryable<T> takeWhile(final LongPredicate2<? super T> filter) {
    return new TakeWhile<>(this, nonNullable(filter, "filter"));
  }
}

final class Skip<S> implements Queryable<S> {
  private final Structable<S> structable;
  private final int until;

  @Contract(pure = true)
  Skip(final Structable<S> structable, final int until) {
    this.structable = structable;
    this.until = until;
  }

  @NotNull
  @Override
  public final Iterator<S> iterator() {
    var skip = 0;
    var seq = new ArrayList<S>();
    for (final var it : structable) if (skip++ >= until) seq.add(it);
    return seq.iterator();
  }
}

final class SkipWhile<S> implements Queryable<S> {
  private final Structable<S> structable;
  private final LongPredicate2<? super S> filter;

  @Contract(pure = true)
  SkipWhile(final Structable<S> structable, final LongPredicate2<? super S> filter) {
    this.structable = structable;
    this.filter = filter;
  }

  @NotNull
  @Override
  public final Iterator<S> iterator() {
    final var result = Many.<S>of();
    var keepSkipping = true;
    var index = 0L;
    for (final var cursor = structable.iterator(); cursor.hasNext(); index++) {
      S value = cursor.next();
      if (!filter.verify(index, value) || !keepSkipping) {
        result.add(value);
        keepSkipping = false;
      }
    }
    return result.iterator();
  }
}

final class Take<S> implements Queryable<S> {
  private final Structable<S> source;
  private final int until;

  @Contract(pure = true)
  Take(final Structable<S> source, final int until) {
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

final class TakeWhile<S> implements Queryable<S> {
  private final Structable<S> some;
  private final LongPredicate2<? super S> expression;

  @Contract(pure = true)
  TakeWhile(final Structable<S> some, final LongPredicate2<? super S> expression) {
    this.some = some;
    this.expression = expression;
  }

  @NotNull
  @Override
  public final Iterator<S> iterator() {
    final var result = Many.<S>of();
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
