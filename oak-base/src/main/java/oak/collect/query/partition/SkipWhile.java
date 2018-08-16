package oak.collect.query.partition;

import oak.collect.query.Queryable;
import oak.func.pre.Predicate1;

import java.util.ArrayList;
import java.util.Iterator;

final class SkipWhile<S> implements Queryable<S> {
  private final Queryable<S> some;
  private final Predicate1<S> expression;

  SkipWhile(Queryable<S> some, Predicate1<S> expression) {
    this.some = some;
    this.expression = expression;
  }

  @Override
  public final Iterator<S> iterator() {
    var s = new ArrayList<S>();
    var keepSkipping = true;
    for (var it : some) {
      if (!expression.test(it) || !keepSkipping) {
        s.add(it);
        keepSkipping = false;
      }
    }
    return s.iterator();
  }
}
