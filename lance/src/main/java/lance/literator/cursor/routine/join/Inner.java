package lance.literator.cursor.routine.join;

import lance.func.Func;
import lance.func.Pred;
import lance.literator.Cursor;
import lance.literator.Literator;
import lance.tuple.Pair;

import java.util.Iterator;
import java.util.Objects;

final class Inner<A, B> implements Join<A, Cursor<Pair<A, B>>> {
  private final Cursor<B> cursor;
  private final Pred.TryBiPredicate<? super A, ? super B> pred;

  Inner(final Cursor<B> cursor) {
    this(
      cursor,
      Objects::equals
    );
  }
  Inner(final Cursor<B> cursor, final Pred.TryBiPredicate<? super A, ? super B> pred) {this.cursor = cursor;
    this.pred = pred;
  }

  @Override
  public Func.TryFunction<A[], Cursor<Pair<A, B>>> onArray() {
    return ts -> cursor.to(new Nested<>(ts, pred));
  }

  @Override
  public Func.TryFunction<Literator<A>, Cursor<Pair<A, B>>> onLiterator() {
    return null;
  }

  @Override
  public Func.TryFunction<Iterator<A>, Cursor<Pair<A, B>>> onIterator() {
    return null;
  }
}
