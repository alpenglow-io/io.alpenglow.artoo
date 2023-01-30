package re.artoo.lance.query.cursor.routine.join;

import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.func.TryPredicate2;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Probe;
import re.artoo.lance.tuple.Pair;
import re.artoo.lance.tuple.Tuple;

import java.util.Arrays;
import java.util.Iterator;

@SuppressWarnings("unchecked")
public final class Nested<R, T> implements Join<R, Cursor<Pair<T, R>>> {
  private final T[] ts;
  private final TryPredicate2<? super T, ? super R> pred;

  Nested(final T[] ts, final TryPredicate2<? super T, ? super R> pred) {
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
  public TryFunction1<R[], Cursor<Pair<T, R>>> onArray() {
    return this::joinArray;
  }

  @Override
  public TryFunction1<Probe<R>, Cursor<Pair<T, R>>> onSource() {
    return null;
  }

  @Override
  public TryFunction1<Iterator<R>, Cursor<Pair<T, R>>> onIterator() {
    return null;
  }
}
