package re.artoo.lance.query.closure;

import re.artoo.lance.func.TryPredicate1;
import re.artoo.lance.query.Closure;

public final class None<T> implements Closure<T, Boolean> {
  private final TryPredicate1<? super T> predicate;
  private boolean noneOfThem;

  public None(final TryPredicate1<? super T> predicate) {
    this.predicate = predicate;
    this.noneOfThem = true;
  }

  @Override
  public Boolean invoke(final T element) throws Throwable {
    return (noneOfThem &= !predicate.invoke(element));
  }

  private static final class NoneOfThem {
    private boolean value = true;
  }
}
