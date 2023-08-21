package re.artoo.lance.query.many;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.func.TryPredicate1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.Many;

import java.util.ArrayList;
import java.util.HashSet;

public interface Settable<T> extends Queryable<T> {
  default Many<T> distinct() {
    return () ->
      cursor()
        .fold(new HashSet<T>(), (set, element) -> add(set, element))
        .flatMap(Cursor::from);
  }

  private HashSet<T> add(HashSet<T> set, T element) {
    set.add(element);
    return set;
  }

  default Many<T> distinct(final TryPredicate1<? super T> where) {
    return () ->
      cursor()
        .fold(new ArrayList<T>(), (list, element) -> add(where, list, element))
        .flatMap(Cursor::from);
  }

  private ArrayList<T> add(TryPredicate1<? super T> where, ArrayList<T> list, T element) throws Throwable {
    if ((where.invoke(element) && !list.contains(element)) || !where.invoke(element)) {
      list.add(element);
    }
    return list;
  }

  /*
  default Many<T> union(final T... steps) {
    return union(Many.pseudo(steps));
  }

  default <Q extends Queryable<T>> Many<T> union(final Q queryable) {
    return Many.pseudo(origin().union(queryable.origin()));
  }

  default Many<T> except(final T... steps) {
    return Many.pseudo(origin().except(steps));
  }

  default Many<T> intersect(final T... steps) {
    return Many.pseudo(origin().intersect(steps));
  }*/
}

@SuppressWarnings("StatementWithEmptyBody")
final class Except<T> implements TryFunction1<T, T> {
  private final Queryable<T> queryable;

  Except(final Queryable<T> queryable) {
    this.queryable = queryable;
  }

  @Override
  public final T invoke(final T origin) throws Throwable {
    final var cursor = queryable.cursor();
    T element = null;
    while (cursor.hasNext() && !(element = cursor.next()).equals(origin)) ;
    return cursor.hasNext() || (element != null && element.equals(origin)) ? null : origin;
  }
}

@SuppressWarnings("StatementWithEmptyBody")
final class Intersect<T> implements TryFunction1<T, T> {
  private final Queryable<T> queryable;

  Intersect(final Queryable<T> queryable) {
    this.queryable = queryable;
  }

  @Override
  public T invoke(final T origin) throws Throwable {
    final var cursor = queryable.cursor();
    var element = cursor.next();
    for (; cursor.hasNext() && !element.equals(origin); element = cursor.next()) ;
    return (element != null && element.equals(origin)) || cursor.hasNext() ? origin : null;
  }
}

