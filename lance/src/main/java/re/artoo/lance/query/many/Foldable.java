package re.artoo.lance.query.many;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.func.TryFunction2;
import re.artoo.lance.func.TryPredicate1;
import re.artoo.lance.query.One;

public interface Foldable<T> extends Queryable<T> {
  default <A, R> One<A> fold(A seed, TryPredicate1<? super T> where, TryFunction1<? super T, ? extends R> select, TryFunction2<? super A, ? super R, ? extends A> aggregator) {
    return () -> cursor()
      .filter(where)
      .map(select)
      .right(seed, aggregator);
  }

  default <A, R> One<A> fold(A seed, TryFunction1<? super T, ? extends R> select, TryFunction2<? super A, ? super R, ? extends A> aggregate) {
    return fold(seed, it -> true, select, aggregate);
  }

  default <A, R> One<A> fold(TryFunction1<? super T, ? extends R> select, TryFunction2<? super A, ? super R, ? extends A> aggregate) {
    return fold(null, it -> true, select, aggregate);
  }

  default <A> One<A> fold(A left, TryFunction2<? super A, ? super T, ? extends A> aggregate) {
    return fold(left, it -> true, it -> it, aggregate);
  }

  default One<T> fold(TryFunction2<? super T, ? super T, ? extends T> aggregate) {
    return fold(null, it -> true, it -> it, aggregate);
  }

}

