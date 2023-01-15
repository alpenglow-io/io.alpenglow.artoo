package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryIntPredicate1;
import re.artoo.lance.func.TryIntPredicate2;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.tuple.Pair;

@FunctionalInterface
public interface Joiner<A> extends Fetcher<A>  {
  default <B, F extends Fetcher<B>> Join<A, B> join(F fetcher) {
    return new Inner<>(this, fetcher);
  }

  interface Join<A, B> extends Fetcher<Pair<A, B>> {
    default Cursor<Pair<A, B>> on(TryIntPredicate2<? super A, ? super B> filter) {
      return
    }
  }
}
