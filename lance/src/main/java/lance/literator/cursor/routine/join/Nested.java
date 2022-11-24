package lance.literator.cursor.routine.join;

import lance.func.Func;
import lance.func.Pred;
import lance.literator.Cursor;
import lance.literator.Literator;
import lance.tuple.Pair;
import lance.tuple.Tuple;

import java.util.Arrays;
import java.util.Iterator;

@SuppressWarnings("unchecked")
public final class Nested<R, T> implements Join<R, Cursor<Pair<T, R>>> {
  private final T[] ts;
  private final Pred.Bi<? super T, ? super R> pred;

  Nested(final T[] ts, final Pred.Bi<? super T, ? super R> pred) {
    this.ts = ts;
    this.pred = pred;
  }

  private Cursor<Pair<T, R>> joinArray(final R[] rs) {
    var joineds = (Pair<T, R>[]) new Pair[0];
    for (final var t : ts) {
      for (final var r : rs) {
        if (pred.test(t, r)) {
          joineds = Arrays.copyOf(joineds, joineds.length + 1);
          joineds[joineds.length - 1] = Tuple.of(t, r);
        }
      }
    }
    return Cursor.open(joineds);
  }

  @Override
  public Func.MaybeFunction<R[], Cursor<Pair<T, R>>> onArray() {
    return this::joinArray;
  }

  @Override
  public Func.MaybeFunction<Literator<R>, Cursor<Pair<T, R>>> onLiterator() {
    return null;
  }

  @Override
  public Func.MaybeFunction<Iterator<R>, Cursor<Pair<T, R>>> onIterator() {
    return null;
  }
}
