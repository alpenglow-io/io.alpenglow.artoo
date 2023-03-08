package re.artoo.lance.query.many;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.func.TryFunction2;
import re.artoo.lance.func.TryIntFunction2;
import re.artoo.lance.func.TryPredicate1;
import re.artoo.lance.query.One;

public interface Aggregatable<LEFT> extends Queryable<LEFT> {
  default <AGGREGATED, SELECTED> One<AGGREGATED> aggregate(AGGREGATED aggregated, TryPredicate1<? super LEFT> where, TryFunction1<? super LEFT, ? extends SELECTED> select, TryFunction2<? super AGGREGATED, ? super SELECTED, ? extends AGGREGATED> operation) {
    return () -> cursor()
      .filter(where)
      .map(select)
      .fold(aggregated, operation);
  }
  default <AGGREGATED, SELECTED> One<AGGREGATED> aggregate(AGGREGATED aggregated, TryPredicate1<? super LEFT> where, TryFunction1<? super LEFT, ? extends SELECTED> select, TryIntFunction2<? super AGGREGATED, ? super SELECTED, ? extends AGGREGATED> operation) {
    return () -> cursor()
      .filter(where)
      .map(select)
      .fold(aggregated, operation);
  }
  default <AGGREGATED, SELECTED> One<AGGREGATED> aggregate(AGGREGATED aggregated, TryFunction1<? super LEFT, ? extends SELECTED> select, TryFunction2<? super AGGREGATED, ? super SELECTED, ? extends AGGREGATED> operation) {
    return () -> cursor().map(select).fold(aggregated, operation);
  }
  default <AGGREGATED, SELECTED> One<AGGREGATED> aggregate(AGGREGATED aggregated, TryFunction1<? super LEFT, ? extends SELECTED> select, TryIntFunction2<? super AGGREGATED, ? super SELECTED, ? extends AGGREGATED> operation) {
    return () -> cursor().map(select).fold(aggregated, operation);
  }
  default <AGGREGATED> One<AGGREGATED> aggregate(AGGREGATED aggregated, TryFunction2<? super AGGREGATED, ? super LEFT, ? extends AGGREGATED> operation) {
    return () -> cursor().fold(aggregated, operation);
  }
  default <AGGREGATED> One<AGGREGATED> aggregate(AGGREGATED aggregated, TryIntFunction2<? super AGGREGATED, ? super LEFT, ? extends AGGREGATED> operation) {
    return () -> cursor().fold(aggregated, operation);
  }
  default One<LEFT> aggregate(TryFunction2<? super LEFT, ? super LEFT, ? extends LEFT> operation) {
    return () -> cursor().reduce(operation);
  }
  default One<LEFT> aggregate(TryIntFunction2<? super LEFT, ? super LEFT, ? extends LEFT> operation) {
    return () -> cursor().reduce(operation);
  }
}

