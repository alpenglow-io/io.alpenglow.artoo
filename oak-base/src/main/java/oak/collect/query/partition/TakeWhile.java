package oak.collect.query.partition;

import oak.collect.query.Queryable;
import oak.func.pre.Predicate1;

import java.util.ArrayList;
import java.util.Iterator;

final class TakeWhile<S> implements Queryable<S> {
  private final Queryable<S> some;
  private final Predicate1<S> expression;

  TakeWhile(final Queryable<S> some, final Predicate1<S> expression) {
    this.some = some;
    this.expression = expression;
  }

  @Override
  public final Iterator<S> iterator() {
    if (!expression.test(some.iterator().next())) return some.iterator();
    
    var s = new ArrayList<S>();
    var keepTaking = true;
    for (var iterator = some.iterator(); iterator.hasNext() && keepTaking;) {
      var it = iterator.next();
      if (expression.test(it)) {
        s.add(it);
      } else {
        keepTaking = false;
      }
    }
    return s.iterator();
  }
}
