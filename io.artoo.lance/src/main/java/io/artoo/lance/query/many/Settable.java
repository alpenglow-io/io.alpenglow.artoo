package io.artoo.lance.query.many;

import io.artoo.lance.func.Func;
import io.artoo.lance.func.Pred;
import io.artoo.lance.query.Many;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.query.oper.Distinct;

@SuppressWarnings("unchecked")
public interface Settable<T> extends Queryable<T> {
  default Many<T> distinct() {
    return distinct(it -> true);
  }

  default Many<T> distinct(final Pred.Uni<? super T> where) {
    return () -> cursor().map(new Distinct<>(where));
  }
/*
  default Many<T> union(final T... elements) {
    return union(Many.from(elements));
  }

  default <Q extends Queryable<T>> Many<T> union(final Q queryable) {
    return Many.of(cursor().union(queryable.cursor()));
  }

  default Many<T> except(final T... elements) {
    return Many.of(cursor().except(elements));
  }

  default Many<T> intersect(final T... elements) {
    return Many.of(cursor().intersect(elements));
  }*/
}

@SuppressWarnings("StatementWithEmptyBody")
final class Except<T> implements Func.Uni<T, T> {
  private final Queryable<T> queryable;

  Except(final Queryable<T> queryable) {this.queryable = queryable;}

  @Override
  public final T tryApply(final T origin) throws Throwable {
    final var cursor = queryable.cursor();
    T element = null;
    while (cursor.hasNext() && !(element = cursor.fetch()).equals(origin));
    return cursor.hasNext() || (element != null && element.equals(origin)) ? null : origin;
  }
}

@SuppressWarnings("StatementWithEmptyBody")
final class Intersect<T> implements Func.Uni<T, T> {
  private final Queryable<T> queryable;

  Intersect(final Queryable<T> queryable) {this.queryable = queryable;}

  @Override
  public T tryApply(final T origin) throws Throwable {
    final var cursor = queryable.cursor();
    var element = cursor.fetch();
    for (; cursor.hasNext() && !element.equals(origin); element = cursor.fetch());
    return (element != null && element.equals(origin)) || cursor.hasNext() ? origin : null;
  }
}

