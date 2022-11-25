package io.alpenglow.artoo.lance.literator.cursor.routine.join;

import io.alpenglow.artoo.lance.func.TryBiPredicate;
import io.alpenglow.artoo.lance.func.TryFunction;
import io.alpenglow.artoo.lance.literator.Cursor;
import io.alpenglow.artoo.lance.literator.Literator;
import io.alpenglow.artoo.lance.tuple.Pair;
import io.alpenglow.artoo.lance.tuple.Tuple;

import java.util.Arrays;
import java.util.Iterator;

@SuppressWarnings("unchecked")
public final class Nested<R, T> implements Join<R, Cursor<Pair<T, R>>> {
  private final T[] ts;
  private final TryBiPredicate<? super T, ? super R> pred;

  Nested(final T[] ts, final TryBiPredicate<? super T, ? super R> pred) {
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
  public TryFunction<R[], Cursor<Pair<T, R>>> onArray() {
    return this::joinArray;
  }

  @Override
  public TryFunction<Literator<R>, Cursor<Pair<T, R>>> onLiterator() {
    return null;
  }

  @Override
  public TryFunction<Iterator<R>, Cursor<Pair<T, R>>> onIterator() {
    return null;
  }
}
