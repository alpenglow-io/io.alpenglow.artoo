package io.artoo.lance.literator.cursor.routine.join;

import io.artoo.lance.func.Func;
import io.artoo.lance.func.Pred;
import io.artoo.lance.literator.Cursor;
import io.artoo.lance.literator.Literator;
import io.artoo.lance.query.many.Joinable;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;

@SuppressWarnings("unchecked")
public final class Nested<R, T> implements Join<R, Cursor<Joinable.Joining.Joined<T, R>>> {
  private final T[] ts;
  private final Pred.Bi<? super T, ? super R> pred;

  Nested(final T[] ts, final Pred.Bi<? super T, ? super R> pred) {
    this.ts = ts;
    this.pred = pred;
  }

  @NotNull
  private Cursor<Joinable.Joining.Joined<T, R>> joinArray(final R[] rs) {
    var joineds = (Joinable.Joining.Joined<T, R>[]) new Joinable.Joining.Joined[0];
    for (final var t : ts) {
      for (final var r : rs) {
        if (pred.test(t, r)) {
          joineds = Arrays.copyOf(joineds, joineds.length + 1);
          joineds[joineds.length - 1] = new Joinable.Joining.Joined<>(t, r);
        }
      }
    }
    return Cursor.open(joineds);
  }

  @Override
  public Func.Uni<R[], Cursor<Joinable.Joining.Joined<T, R>>> onArray() {
    return this::joinArray;
  }

  @Override
  public Func.Uni<Literator<R>, Cursor<Joinable.Joining.Joined<T, R>>> onLiterator() {
    return null;
  }

  @Override
  public Func.Uni<Iterator<R>, Cursor<Joinable.Joining.Joined<T, R>>> onIterator() {
    return null;
  }
}
