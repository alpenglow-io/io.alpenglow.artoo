package re.artoo.lance.query.closure;

import re.artoo.lance.func.TryPredicate1;
import re.artoo.lance.query.Closure;

public final class Every<T> implements Closure<T, Boolean> {
  private final TryPredicate1<? super T> where;
  private boolean allOfThem;
  public Every(final TryPredicate1<? super T> where) {
    assert where != null;
    this.where = where;
    this.allOfThem = true;
  }

  @Override
  public Boolean invoke(T element) throws Throwable {
    return allOfThem &= where.invoke(element);
  }
}
