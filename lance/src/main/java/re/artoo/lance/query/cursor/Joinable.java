package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryPredicate2;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.tuple.Pair;
import re.artoo.lance.tuple.Tuple;
import re.artoo.lance.value.Array;

import java.util.Objects;

public sealed interface Joinable<FIRST> extends Probe<FIRST> permits Cursor {
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
  }
}

final class Left<FIRST, SECOND> implements Join<FIRST, SECOND> {
  private final Probe<FIRST> left;
  private final Probe<SECOND> right;
  private final TryPredicate2<? super FIRST, ? super SECOND> condition;

  Left(Probe<FIRST> left, Probe<SECOND> right) {
    this(left, right, Objects::deepEquals);
  }

  private Left(Probe<FIRST> left, Probe<SECOND> right, TryPredicate2<? super FIRST, ? super SECOND> condition) {
    this.left = left;
    this.right = right;
    this.condition = condition;
  }

  private Probe<SECOND> rightProbe;
  private FIRST first;

  @Override
  public Pair<FIRST, SECOND> tick() throws Throwable {
    Pair<FIRST, SECOND> pair = null;
    if (left.hasNext()) {
      if (rightProbe == null || !rightProbe.hasNext()) {
        first = left.tick();
        rightProbe = right.rewind();
      }
      pair = Tuple.of(first, null);
      for (var second = rightProbe.tick(); rightProbe.hasNext(); second = rightProbe.tick()) {
        if (condition.invoke(first, second)) {
          pair = Tuple.of(first, second);
        }
      }
    }
    return pair;
  }

  @Override
  public boolean isTickable() throws Throwable {
    return left.isTickable();
  }

  @Override
  public Cursor<Pair<FIRST, SECOND>> on(TryPredicate2<? super FIRST, ? super SECOND> condition) {
    return new Left<>(left, right, condition);
  }
}

final class Right<FIRST, SECOND> implements Join<FIRST, SECOND> {
  private final Probe<FIRST> left;
  private final Probe<SECOND> right;
  private final TryPredicate2<? super FIRST, ? super SECOND> condition;
  private Array<Pair<FIRST, SECOND>> scrolled = Array.none();

  Right(Probe<FIRST> left, Probe<SECOND> right) {
    this(left, right, Objects::deepEquals);
  }
  Right(Probe<FIRST> left, Probe<SECOND> right, TryPredicate2<? super FIRST, ? super SECOND> condition) {
    this.left = left;
    this.right = right;
    this.condition = condition;
  }

  private Probe<FIRST> leftProbe;
  private SECOND second;

  @Override
  public boolean isTickable() throws Throwable {
    return right.isTickable();
  }

  @Override
  public Pair<FIRST, SECOND> tick() throws Throwable {
    Pair<FIRST, SECOND> pair = null;
    if (right.hasNext()) {
      if (leftProbe == null || !leftProbe.hasNext()) {
        second = right.tick();
        leftProbe = left.rewind();
      }
      pair = Tuple.of(null, second);
      for (var first = leftProbe.tick(); leftProbe.hasNext(); first = leftProbe.tick()) {
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
  public Pair<FIRST, SECOND> tick() throws Throwable {
    return left.hasNext()
      ? traverseLeft()
      : traverseRight();
  }

  private Pair<FIRST, SECOND> traverseRight() throws Throwable {
    Pair<FIRST, SECOND> joined = right.tick();
    while (scrolled.includes(joined)) joined = right.tick();
    return joined;
  }

  private Pair<FIRST, SECOND> traverseLeft() throws Throwable {
    return (scrolled = scrolled.push(left.tick())).findLast();
  }

  @Override
  public boolean isTickable() throws Throwable {
    return left.isTickable() || right.isTickable();
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
  public boolean isTickable() throws Throwable {
    return outer.isTickable();
  }

  @Override
  public Pair<FIRST, SECOND> tick() throws Throwable {
    return outer.filter(pair -> pair.first() != null && pair.second() != null).tick();
  }

  @Override
  public Cursor<Pair<FIRST, SECOND>> on(TryPredicate2<? super FIRST, ? super SECOND> condition) {
    return new Inner<>((Join<FIRST, SECOND>) outer.on(condition));
  }
}
