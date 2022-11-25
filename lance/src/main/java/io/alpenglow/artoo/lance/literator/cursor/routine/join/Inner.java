package io.alpenglow.artoo.lance.literator.cursor.routine.join;

import io.alpenglow.artoo.lance.func.TryBiPredicate;
import io.alpenglow.artoo.lance.func.TryFunction;
import io.alpenglow.artoo.lance.literator.Cursor;
import io.alpenglow.artoo.lance.literator.Literator;
import io.alpenglow.artoo.lance.tuple.Pair;

import java.util.Iterator;
import java.util.Objects;

final class Inner<A, B> implements Join<A, Cursor<Pair<A, B>>> {
  private final Cursor<B> cursor;
  private final TryBiPredicate<? super A, ? super B> pred;

  Inner(final Cursor<B> cursor) {
    this(
      cursor,
      Objects::equals
    );
  }
  Inner(final Cursor<B> cursor, final TryBiPredicate<? super A, ? super B> pred) {this.cursor = cursor;
    this.pred = pred;
  }

  @Override
  public TryFunction<A[], Cursor<Pair<A, B>>> onArray() {
    return ts -> cursor.to(new Nested<>(ts, pred));
  }

  @Override
  public TryFunction<Literator<A>, Cursor<Pair<A, B>>> onLiterator() {
    return null;
  }

  @Override
  public TryFunction<Iterator<A>, Cursor<Pair<A, B>>> onIterator() {
    return null;
  }
}
