package re.artoo.lance.query.many;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.func.TryFunction2;
import re.artoo.lance.func.TryPredicate1;
import re.artoo.lance.query.One;

public interface Foldable<T> extends Queryable<T> {
  default <FOLDED, R> One<FOLDED> fold(FOLDED right, TryPredicate1<? super T> where, TryFunction1<? super T, ? extends R> select, TryFunction2<? super FOLDED, ? super R, ? extends FOLDED> operation) {
    return () -> cursor()
      .filter(where)
      .map(select)
      .foldRight(right, operation);
  }

  default <FOLDED, R> One<FOLDED> fold(FOLDED right, TryFunction1<? super T, ? extends R> select, TryFunction2<? super FOLDED, ? super R, ? extends FOLDED> operation) {
    return () -> cursor().map(select).foldRight(right, operation);
  }

  default <FOLDED> One<FOLDED> fold(FOLDED right, TryFunction2<? super FOLDED, ? super T, ? extends FOLDED> operation) {
    return () -> cursor().foldRight(right, operation);
  }

  default One<T> fold(TryFunction2<? super T, ? super T, ? extends T> operation) {
    return () -> cursor().reduceRight(operation);
  }

}

