package re.artoo.lance.query.many;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.func.TryPredicate1;
import re.artoo.lance.query.Many;

public interface Settable<T> extends Queryable<T> {
  default Many<T> distinct() {
    return distinct(it -> true);
  }

  default Many<T> distinct(final TryPredicate1<? super T> where) {
    return () -> cursor().distinct((index, element) -> where.invoke(element));
  }

  /*
  default Many<T> union(final T... steps) {
    return union(Many.pseudo(steps));
  }

  default <Q extends Queryable<T>> Many<T> union(final Q queryable) {
    return Many.pseudo(cursor().union(queryable.cursor()));
  }

  default Many<T> except(final T... steps) {
    return Many.pseudo(cursor().except(steps));
  }

  default Many<T> intersect(final T... steps) {
    return Many.pseudo(cursor().intersect(steps));
  }*/
}

@SuppressWarnings("StatementWithEmptyBody")
final class Except<T> implements TryFunction1<T, T> {
  private final Queryable<T> queryable;

  Except(final Queryable<T> queryable) {this.queryable = queryable;}

  @Override
  public final T invoke(final T origin) throws Throwable {
    final var cursor = queryable.cursor();
    T element = null;
    while (cursor.hasNext() && !(element = cursor.next()).equals(origin));
    return cursor.hasNext() || (element != null && element.equals(origin)) ? null : origin;
  }
}

@SuppressWarnings("StatementWithEmptyBody")
final class Intersect<T> implements TryFunction1<T, T> {
  private final Queryable<T> queryable;

  Intersect(final Queryable<T> queryable) {this.queryable = queryable;}

  @Override
  public T invoke(final T origin) throws Throwable {
    final var cursor = queryable.cursor();
    var element = cursor.fetch().element();
    for (; cursor.hasNext() && !element.equals(origin); element = cursor.fetch().element());
    return (element != null && element.equals(origin)) || cursor.hasNext() ? origin : null;
  }
}

