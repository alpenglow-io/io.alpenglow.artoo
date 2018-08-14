package oak.collect.query.partition;

import io.ibex.collect.seq.Sequence;
import oak.func.pre.Predicate1;
import oak.collect.query.Queryable;

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
    var s = Sequence.<S>empty();
    var keepSkipping = true;
    for (var it : some) {
      if (!expression.test(it) || !keepSkipping) {
        s = s.add(it);
        keepSkipping = false;
      }
    }
    return s.iterator();
  }
}
