package re.artoo.lance.query.many;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.func.TryPredicate1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.Many;

import java.util.ArrayList;

public interface Settable<T> extends Queryable<T> {
  default Many<T> distinct() {
    return distinct(it -> true);
  }

  default Many<T> distinct(final TryPredicate1<? super T> where) {
    return () -> cursor()
      .filter(where)
      .reduce(new ArrayList<T>(), (array, element) -> array.contains(element) ? array : addTo(array, element))
      .flatMap(Cursor::from);
  }

  private ArrayList<T> addTo(ArrayList<T> array, T element) {
    array.add(element);
    return array;
  }
/*
  default Many<T> union(final T... elements) {
    return union(Many.pseudo(elements));
  }

  default <Q extends Queryable<T>> Many<T> union(final Q queryable) {
    return Many.pseudo(cursor().union(queryable.cursor()));
  }

  default Many<T> except(final T... elements) {
    return Many.pseudo(cursor().except(elements));
  }

  default Many<T> intersect(final T... elements) {
    return Many.pseudo(cursor().intersect(elements));
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
    while (cursor.hasNext() && !(element = cursor.fetch()).equals(origin));
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
    var element = cursor.fetch();
    for (; cursor.hasNext() && !element.equals(origin); element = cursor.fetch());
    return (element != null && element.equals(origin)) || cursor.hasNext() ? origin : null;
  }
}

