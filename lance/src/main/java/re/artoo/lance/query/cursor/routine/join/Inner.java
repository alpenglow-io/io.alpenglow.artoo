package re.artoo.lance.query.cursor.routine.join;

import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.func.TryPredicate2;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Inquiry;
import re.artoo.lance.tuple.Pair;

import java.util.Iterator;
import java.util.Objects;

final class Inner<A, B> implements Join<A, Cursor<Pair<A, B>>> {
  private final Cursor<B> cursor;
  private final TryPredicate2<? super A, ? super B> pred;

  Inner(final Cursor<B> cursor) {
    this(
      cursor,
      Objects::equals
    );
  }
  Inner(final Cursor<B> cursor, final TryPredicate2<? super A, ? super B> pred) {this.cursor = cursor;
    this.pred = pred;
  }

  @Override
  public TryFunction1<A[], Cursor<Pair<A, B>>> onArray() {
    return ts -> cursor.to(new Nested<>(ts, pred));
  }

  @Override
  public TryFunction1<Inquiry<A>, Cursor<Pair<A, B>>> onSource() {
    return null;
  }

  @Override
  public TryFunction1<Iterator<A>, Cursor<Pair<A, B>>> onIterator() {
    return null;
  }
}
