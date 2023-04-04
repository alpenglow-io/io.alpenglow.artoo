package re.artoo.lance.query.cursor;

import re.artoo.lance.query.Cursor;

public sealed interface Joinable<FIRST> extends Fetch<FIRST> permits Cursor {
  /*
  default <SECOND> Join<FIRST, SECOND> outerJoin(Cursor<SECOND> right) {
    return new Outer<>(new Left<>(this, right), new Right<>(this, right));
  }
  default <SECOND> Join<FIRST, SECOND> leftJoin(Cursor<SECOND> right) {
    return new Left<>(this, right);
  }
  default <SECOND> Join<FIRST, SECOND> rightJoin(Cursor<SECOND> right) {
    return new Right<>(this, right);
  }
  default <SECOND> Join<FIRST, SECOND> innerJoin(Cursor<SECOND> right) {
    return outerJoin(right);
  }*/
}
/*

final class Left<FIRST, SECOND> implements Join<FIRST, SECOND> {
  private final Fetch<FIRST> left;
  private final Fetch<SECOND> right;
  private final TryPredicate2<? super FIRST, ? super SECOND> condition;

  Left(Fetch<FIRST> left, Fetch<SECOND> right) {
    this(left, right, Objects::deepEquals);
  }

  private Left(Fetch<FIRST> left, Fetch<SECOND> right, TryPredicate2<? super FIRST, ? super SECOND> condition) {
    this.left = left;
    this.right = right;
    this.condition = condition;
  }

  private Fetch<SECOND> rightProbe;
  private FIRST first;

  @Override
  public Pair<FIRST, SECOND> fetch() throws Throwable {
    Pair<FIRST, SECOND> pair = null;
    if (left.hasNext()) {
      if (rightProbe == null || !rightProbe.hasNext()) {
        first = left.fetch();
        rightProbe = right.rewind();
      }
      pair = Tuple.of(first, null);
      for (var second = rightProbe.fetch(); rightProbe.hasNext(); second = rightProbe.fetch()) {
        if (condition.invoke(first, second)) {
          pair = Tuple.of(first, second);
        }
      }
    }
    return pair;
  }

  @Override
  public boolean canFetch() throws Throwable {
    return left.canFetch();
  }

  @Override
  public Cursor<Pair<FIRST, SECOND>> on(TryPredicate2<? super FIRST, ? super SECOND> condition) {
    return new Left<>(left, right, condition);
  }
}

final class Right<FIRST, SECOND> implements Join<FIRST, SECOND> {
  private final Fetch<FIRST> left;
  private final Fetch<SECOND> right;
  private final TryPredicate2<? super FIRST, ? super SECOND> condition;
  private Array<Pair<FIRST, SECOND>> scrolled = Array.none();

  Right(Fetch<FIRST> left, Fetch<SECOND> right) {
    this(left, right, Objects::deepEquals);
  }
  Right(Fetch<FIRST> left, Fetch<SECOND> right, TryPredicate2<? super FIRST, ? super SECOND> condition) {
    this.left = left;
    this.right = right;
    this.condition = condition;
  }

  private Fetch<FIRST> leftProbe;
  private SECOND second;

  @Override
  public boolean canFetch() throws Throwable {
    return right.canFetch();
  }

  @Override
  public Pair<FIRST, SECOND> fetch() throws Throwable {
    Pair<FIRST, SECOND> pair = null;
    if (right.hasNext()) {
      if (leftProbe == null || !leftProbe.hasNext()) {
        second = right.fetch();
        leftProbe = left.rewind();
      }
      pair = Tuple.of(null, second);
      for (var first = leftProbe.fetch(); leftProbe.hasNext(); first = leftProbe.fetch()) {
        if (condition.invoke(first, second)) {
          pair = Tuple.of(first, second);
        }
      }
    }
    return pair;
  }

  @Override
  public Cursor<Pair<FIRST, SECOND>> on(TryPredicate2<? super FIRST, ? super SECOND> condition) {
    return new Right<>(left, right, condition);
  }
}

final class Outer<FIRST, SECOND> implements Join<FIRST, SECOND> {
  private final Join<FIRST, SECOND> left;
  private final Join<FIRST, SECOND> right;

  Outer(Join<FIRST, SECOND> left, Join<FIRST, SECOND> right) {
    this.left = left;
    this.right = right;
  }

  private Array<Pair<FIRST, SECOND>> scrolled = Array.none();

  @Override
  public Pair<FIRST, SECOND> fetch() throws Throwable {
    return left.hasNext()
      ? traverseLeft()
      : traverseRight();
  }

  private Pair<FIRST, SECOND> traverseRight() throws Throwable {
    Pair<FIRST, SECOND> joined = right.fetch();
    while (scrolled.includes(joined)) joined = right.fetch();
    return joined;
  }

  private Pair<FIRST, SECOND> traverseLeft() throws Throwable {
    return (scrolled = scrolled.push(left.fetch())).findLast().orElseThrow();
  }

  @Override
  public boolean canFetch() throws Throwable {
    return left.canFetch() || right.canFetch();
  }

  @Override
  public Cursor<Pair<FIRST, SECOND>> on(TryPredicate2<? super FIRST, ? super SECOND> condition) {
    return new Outer<>((Join<FIRST, SECOND>) left.on(condition), (Join<FIRST, SECOND>) right.on(condition));
  }
}

final class Inner<FIRST, SECOND> implements Join<FIRST, SECOND> {
  private final Join<FIRST, SECOND> outer;

  Inner(Join<FIRST, SECOND> outer) {
    this.outer = outer;
  }

  @Override
  public boolean canFetch() throws Throwable {
    return outer.canFetch();
  }

  @Override
  public Pair<FIRST, SECOND> fetch() throws Throwable {
    return outer.filter(pair -> pair.first() != null && pair.second() != null).fetch();
  }

  @Override
  public Cursor<Pair<FIRST, SECOND>> on(TryPredicate2<? super FIRST, ? super SECOND> condition) {
    return new Inner<>((Join<FIRST, SECOND>) outer.on(condition));
  }
}
*/
