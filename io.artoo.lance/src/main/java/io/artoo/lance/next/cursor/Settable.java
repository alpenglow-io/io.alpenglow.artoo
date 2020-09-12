package io.artoo.lance.next.cursor;

import io.artoo.lance.func.Func;
import io.artoo.lance.func.Pred;
import io.artoo.lance.next.Cursor;
import io.artoo.lance.next.Next;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@SuppressWarnings("unchecked")
public interface Settable<T> extends Concatenatable<T> {
  default Cursor<T> distinct(final Pred.Uni<? super T> where) {
    return select(new Distinct<>(where));
  }

  default Cursor<T> union(final T... elements) {
    return union(Cursor.every(elements));
  }

  default <N extends Next<T>> Cursor<T> union(final N next) {
    return concat(next).select(new Distinct<>(it -> true));
  }

  default Cursor<T> except(final T... elements) {
    return select(new Except<>(elements));
  }

  default Cursor<T> intersect(final T... elements) {
    return select(new Intersect<>(elements));
  }
}

final class Except<T> implements Func.Uni<T, T> {
  private final T[] elements;

  Except(final T[] elements) {this.elements = elements;}

  @Override
  public final T tryApply(final T origin) {
    var search = 0;
    while (search < elements.length && !Objects.equals(elements[search], origin)) { search++; }
    return search == elements.length ? origin : null;
  }
}

final class Intersect<T> implements Func.Uni<T, T> {
  private final T[] elements;

  Intersect(final T[] elements) {this.elements = elements;}

  @Override
  public T tryApply(final T origin) {
    var search = 0;
    while (search < elements.length && !Objects.equals(elements[search], origin)) { search++; }
    return search == elements.length ? null : origin;
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
