package io.artoo.lance.query.many.oftwo;

import io.artoo.lance.literator.cursor.routine.concat.Concat;
import io.artoo.lance.query.Many;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.tuple.Pair;

public interface Concatenatable<A, B> extends Queryable.OfTwo<A, B> {
  @SuppressWarnings("unchecked")
  default <P extends Pair<A, B>> Many.OfTwo<A, B> concat(final P... tuples) {
    return () -> cursor().to(Concat.array(tuples));
  }

  default <Q extends Queryable.OfTwo<A, B>> Many.OfTwo<A, B> concat(final Q queryable) {
    return () -> cursor().to(Concat.liter(queryable.cursor()));
  }
}

