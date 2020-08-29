package io.artoo.lance.next.cursor;

import io.artoo.lance.func.Func;
import io.artoo.lance.func.Pred;
import io.artoo.lance.next.Cursor;
import io.artoo.lance.next.Next;

import java.util.ArrayList;
import java.util.Collection;

@SuppressWarnings("unchecked")
public interface Settable<T> extends Concatenatable<T> {
  default Cursor<T> distinct() {
    return distinct(it -> true);
  }

  default Cursor<T> distinct(final Pred.Uni<? super T> where) {
    return select(new Distinct<T>(where));
  }

  default Cursor<T> union(final T... elements) {
    return union(Cursor.every(elements));
  }

  default <N extends Next<T>> Cursor<T> union(final N next) {
    return concat(next).select(new Distinct<>(it -> true));
  }

  default Cursor<T> except(final T... elements) {
    return except(Cursor.every(elements));
  }

  default <N extends Next<T>> Cursor<T> except(final N next) {
    return select(new Except<>(next));
  }

  default Cursor<T> intersect(final T... elements) {
    return intersect(Cursor.every(elements));
  }

  default <N extends Next<T>> Cursor<T> intersect(final N next) {
    return select(new Intersect<>(next));
  }
}

@SuppressWarnings("StatementWithEmptyBody")
final class Except<T> implements Func.Uni<T, T> {
  private final Next<T> next;

  Except(final Next<T> next) {this.next = next;}

  @Override
  public final T tryApply(final T origin) throws Throwable {
    T element = null;
    while (next.hasNext() && !(element = next.fetch()).equals(origin));
    return next.hasNext() || (element != null && element.equals(origin)) ? null : origin;
  }
}

@SuppressWarnings("StatementWithEmptyBody")
final class Intersect<T> implements Func.Uni<T, T> {
  private final Next<T> next;

  Intersect(final Next<T> next) {this.next = next;}

  @Override
  public T tryApply(final T origin) throws Throwable {
    var element = next.fetch();
    for (; next.hasNext() && !element.equals(origin); element = next.fetch());
    return (element != null && element.equals(origin)) || next.hasNext() ? origin : null;
  }
}

final class Distinct<T> implements Func.Uni<T, T> {
  private final Pred.Uni<? super T> where;
  private final Collection<T> collected;

  Distinct(final Pred.Uni<? super T> where) {
    assert where != null;
    this.where = where;
    this.collected = new ArrayList<>();
  }

  @Override
  public final T tryApply(final T element) throws Throwable {
    if (where.tryTest(element) && !collected.contains(element)) {
      collected.add(element);
      return element;
    } else if (where.tryTest(element) && collected.contains(element)) {
      return null;
    } else {
      return element;
    }
  }
}
