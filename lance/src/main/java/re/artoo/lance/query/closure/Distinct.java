package re.artoo.lance.query.closure;

import re.artoo.lance.func.TryPredicate1;
import re.artoo.lance.query.Closure;

import java.util.ArrayList;
import java.util.Collection;

public final class Distinct<T> implements Closure<T, T> {
  private final TryPredicate1<? super T> where;
  private final Collection<T> collected;

  public Distinct(final TryPredicate1<? super T> where) {
    assert where != null;
    this.where = where;
    this.collected = new ArrayList<>();
  }

  @Override
  public T invoke(T element) throws Throwable {
    final boolean expected = where.invoke(element);
    if (expected && collected.contains(element)) {
      return null;
    } else if (expected && !collected.contains(element)) {
      collected.add(element);
    }

    return element;
  }
}
