package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.func.TryPredicate2;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.tuple.Pair;
import re.artoo.lance.tuple.Tuple;
import re.artoo.lance.value.Array;

import java.util.Objects;

public sealed interface Joinable<FIRST> extends Head<FIRST>, Tail<FIRST> permits Cursor {
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
  private final Head<FIRST> left;
  private final Head<SECOND> right;
  private final TryPredicate2<? super FIRST, ? super SECOND> condition;
  private Array<Pair<FIRST, SECOND>> scrolled = Array.empty();
  private Head<SECOND> rightHead;

  Left(Head<FIRST> left, Head<SECOND> right) {
    this(left, right, Objects::deepEquals);
  }

  private Left(Head<FIRST> left, Head<SECOND> right, TryPredicate2<? super FIRST, ? super SECOND> condition) {
    this.left = left;
    this.right = right;
    this.condition = condition;
  }

  @Override
  public <R> R scroll(TryIntFunction1<? super Pair<FIRST, SECOND>, ? extends R> fetch) throws Throwable {
    Pair<FIRST, SECOND> pair = null;
    if (left.hasNext()) {
      if (rightHead == null || !rightHead.hasNext()) rightHead = right.head();
      pair = Tuple.of(left.scroll(), null);
      final var before = scrolled.length();
      while (rightHead.hasNext() && before == scrolled.length()) {
        final var second = right.scroll();
        if (condition.invoke(pair.first(), second)) scrolled = scrolled.push((pair = Tuple.of(pair.first(), second)));
      }
    }
    return fetch.invoke(0, pair);
  }

  @Override
  public boolean hasNext() {
    return left.hasNext();
  }

  @Override
  public boolean hasPrior() {
    return false;
  }

  @Override
  public <R> R scrollback(TryIntFunction1<? super Pair<FIRST, SECOND>, ? extends R> fetch) throws Throwable {
    return null;
  }

  @Override
  public Cursor<Pair<FIRST, SECOND>> on(TryPredicate2<? super FIRST, ? super SECOND> condition) {
    return new Left<>(left, right, condition);
  }
}

final class Right<FIRST, SECOND> implements Join<FIRST, SECOND> {
  private final Head<FIRST> left;
  private final Head<SECOND> right;
  private final TryPredicate2<? super FIRST, ? super SECOND> condition;
  private Array<Pair<FIRST, SECOND>> scrolled = Array.empty();
  private Head<FIRST> leftHead;

  Right(Head<FIRST> left, Head<SECOND> right) {
    this(left, right, Objects::deepEquals);
  }
  Right(Head<FIRST> left, Head<SECOND> right, TryPredicate2<? super FIRST, ? super SECOND> condition) {
    this.left = left;
    this.right = right;
    this.condition = condition;
  }

  @Override
  public <R> R scroll(TryIntFunction1<? super Pair<FIRST, SECOND>, ? extends R> fetch) throws Throwable {
    Pair<FIRST, SECOND> pair = null;
    if (right.hasNext()) {
      if (leftHead == null || !leftHead.hasNext()) leftHead = left.head();
      pair = Tuple.of(null, right.scroll());
      final var before = scrolled.length();
      while (leftHead.hasNext() && before == scrolled.length()) {
        final var first = left.scroll();
        final var found = Tuple.of(first, pair.second());
        if (condition.invoke(first, pair.second()) && scrolled.notContains(found)) scrolled = scrolled.push(found);
      }
    }

    return fetch.invoke(0, pair);
  }

  @Override
  public boolean hasNext() {
    return right.hasNext();
  }

  @Override
  public boolean hasPrior() {
    return false;
  }

  @Override
  public <R> R scrollback(TryIntFunction1<? super Pair<FIRST, SECOND>, ? extends R> fetch) throws Throwable {
    return null;
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

  @Override
  public boolean hasPrior() {
    return false;
  }

  @Override
  public <R> R scroll(TryIntFunction1<? super Pair<FIRST, SECOND>, ? extends R> fetch) throws Throwable {
    return left.hasNext() ? fetch.invoke(0, left.scroll()) : fetch.invoke(0, right.scroll());
  }

  @Override
  public boolean hasNext() {
    return left.hasNext() || right.hasNext();
  }

  @Override
  public <R> R scrollback(TryIntFunction1<? super Pair<FIRST, SECOND>, ? extends R> fetch) throws Throwable {
    return null;
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
  public boolean hasPrior() {
    return false;
  }

  @Override
  public <R> R scroll(TryIntFunction1<? super Pair<FIRST, SECOND>, ? extends R> fetch) throws Throwable {
    return outer.filter(pair -> pair.first() != null && pair.second() != null).scroll(fetch);
  }

  @Override
  public Cursor<Pair<FIRST, SECOND>> on(TryPredicate2<? super FIRST, ? super SECOND> condition) {
    return new Inner<>((Join<FIRST, SECOND>) outer.on(condition));
  }

  @Override
  public boolean hasNext() {
    return outer.hasNext();
  }

  @Override
  public <R> R scrollback(TryIntFunction1<? super Pair<FIRST, SECOND>, ? extends R> fetch) throws Throwable {
    return null;
  }
}
