package io.artoo.lance.literator.cursor.routine.join;

import io.artoo.lance.func.Func;
import io.artoo.lance.func.Pred;
import io.artoo.lance.literator.Cursor;
import io.artoo.lance.literator.Literator;
import io.artoo.lance.query.many.Joinable;
import io.artoo.lance.tuple.Tuple;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;

@SuppressWarnings("unchecked")
public final class Nested<R, T> implements Join<R, Cursor<Tuple.OfTwo<T, R>>> {
  private final T[] ts;
  private final Pred.Bi<? super T, ? super R> pred;

  Nested(final T[] ts, final Pred.Bi<? super T, ? super R> pred) {
    this.ts = ts;
    this.pred = pred;
  }

  @NotNull
  private Cursor<Tuple.OfTwo<T, R>> joinArray(final R[] rs) {
    var joineds = (Tuple.OfTwo<T, R>[]) new Tuple.OfTwo[0];
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
  public Func.Uni<R[], Cursor<Tuple.OfTwo<T, R>>> onArray() {
    return this::joinArray;
  }

  @Override
  public Func.Uni<Literator<R>, Cursor<Tuple.OfTwo<T, R>>> onLiterator() {
    return null;
  }

  @Override
  public Func.Uni<Iterator<R>, Cursor<Tuple.OfTwo<T, R>>> onIterator() {
    return null;
  }
}
