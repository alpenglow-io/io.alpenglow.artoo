package re.artoo.lance.query.many.pairs;

import re.artoo.lance.Queryable;
import re.artoo.lance.query.Many;
import re.artoo.lance.query.cursor.routine.concat.Concat;
import re.artoo.lance.tuple.Pair;

public interface Concatenatable<A, B> extends Queryable.OfTwo<A, B> {
  @SuppressWarnings("unchecked")
  default <P extends Pair<A, B>> Many.Pairs<A, B> concat(final P... tuples) {
    return () -> cursor().to(Concat.array(tuples));
  }

  default <Q extends Queryable.OfTwo<A, B>> Many.Pairs<A, B> concat(final Q queryable) {
    return () -> cursor().to(Concat.liter(queryable.cursor()));
  }
}

