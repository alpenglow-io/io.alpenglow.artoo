package oak.collect.query;

import oak.func.pre.Predicate1;

import java.util.ArrayList;
import java.util.Iterator;

import static java.util.Objects.requireNonNull;

public interface Partitioning<T, M extends Iterable<T>> extends Iterable<T> {
  static <S> Queryable<S> skip(final Queryable<S> source, final int until) {
    return new Skip<>(
      requireNonNull(source, "Source is null"),
      until
    );
  }

  static <S> Queryable<S> take(final Queryable<S> source, final int until) {
    return new Take<>(
      requireNonNull(source, "Source is null"),
      until
    );
  }

  static <S> Queryable<S> skipWhile(final Queryable<S> source, final Predicate1<S> expression) {
    return new SkipWhile<>(
      requireNonNull(source, "Source is null"),
      requireNonNull(expression, "Expression is null")
    );
  }

  static <S> Queryable<S> takeWhile(final Queryable<S> source, final Predicate1<S> expression) {
    return new TakeWhile<>(
      requireNonNull(source, "Source is null"),
      requireNonNull(expression, "Expression is null")
    );
  }
}

final class Skip<S> implements Partitioning<S, Queryable<S>>, Queryable<S> {
  private final Queryable<S> source;
  private final int until;

  Skip(final Queryable<S> source, final int until) {
    this.source = source;
    this.until = until;
  }

  @Override
  public Iterator<S> iterator() {
    var skip = 0;
    var seq = new ArrayList<S>();
    for (final var it : source) if (skip++ >= until) seq.add(it);
    return seq.iterator();
  }
}

final class SkipWhile<S> implements Queryable<S> {
  private final Queryable<S> some;
  private final Predicate1<S> expression;

  SkipWhile(Queryable<S> some, Predicate1<S> expression) {
    this.some = some;
    this.expression = expression;
  }

  @Override
  public final Iterator<S> iterator() {
    var s = new ArrayList<S>();
    var keepSkipping = true;
    for (var it : some) {
      if (!expression.test(it) || !keepSkipping) {
        s.add(it);
        keepSkipping = false;
      }
    }
    return s.iterator();
  }
}

final class Take<S> implements Queryable<S> {
  private final Queryable<S> source;
  private final int until;

  Take(final Queryable<S> source, final int until) {
    this.source = source;
    this.until = until;
  }

  @Override
  public final Iterator<S> iterator() {
    var take = 0;
    var seq = new ArrayList<S>();
    for (final var it : source) if (take++ < until) seq.add(it);
    return seq.iterator();
  }
}

final class TakeWhile<S> implements Queryable<S> {
  private final Queryable<S> some;
  private final Predicate1<S> expression;

  TakeWhile(final Queryable<S> some, final Predicate1<S> expression) {
    this.some = some;
    this.expression = expression;
  }

  @Override
  public final Iterator<S> iterator() {
    if (!expression.test(some.iterator().next())) return some.iterator();

    var s = new ArrayList<S>();
    var keepTaking = true;
    for (var iterator = some.iterator(); iterator.hasNext() && keepTaking;) {
      var it = iterator.next();
      if (expression.test(it)) {
        s.add(it);
      } else {
        keepTaking = false;
      }
    }
    return s.iterator();
  }
}
