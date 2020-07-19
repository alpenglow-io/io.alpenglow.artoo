package io.artoo.lance.query.many;

import io.artoo.lance.func.Func;
import io.artoo.lance.func.Pred;
import io.artoo.lance.query.Many;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.query.operation.Distinct;

import static io.artoo.lance.query.Many.resultSet;
import static io.artoo.lance.type.Nullability.nonNullable;

@SuppressWarnings("unchecked")
public interface Settable<T> extends Queryable<T> {
  default Many<T> distinct() {
    return distinct(it -> true);
  }

  default Many<T> distinct(final Pred.Uni<? super T> where) {
    final var w = nonNullable(where, "where");
    return () -> cursor().map(new Distinct<T>(w).butNulls());
  }

  default Many<T> union(final T... elements) {
    return union(Many.from(elements));
  }

  default <Q extends Queryable<T>> Many<T> union(final Q queryable) {
    return () -> cursor()
      .concat(queryable.cursor())
      .map(new Distinct<>(it -> true));
  }

  default Many<T> except(final T... elements) {
    return except(Many.from(elements));
  }

  default <Q extends Queryable<T>> Many<T> except(final Q queryable) {
    return resultSet(() -> cursor().map(new Except<>(queryable)));
  }

  default Many<T> intersect(final T... elements) {
    return intersect(Many.from(elements));
  }

  default <Q extends Queryable<T>> Many<T> intersect(final Q queryable) {
    return resultSet(() -> cursor().map(new Intersect<>(queryable)));
  }
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

