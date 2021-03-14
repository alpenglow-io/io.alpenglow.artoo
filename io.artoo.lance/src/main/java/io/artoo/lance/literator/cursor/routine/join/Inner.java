package io.artoo.lance.literator.cursor.routine.join;

import io.artoo.lance.func.Func;
import io.artoo.lance.func.Pred;
import io.artoo.lance.literator.Cursor;
import io.artoo.lance.literator.Literator;
import io.artoo.lance.tuple.Pair;

import java.util.Iterator;
import java.util.Objects;

final class Inner<A, B> implements Join<A, Cursor<Pair<A, B>>> {
  private final Cursor<B> cursor;
  private final Pred.Bi<? super A, ? super B> pred;

  Inner(final Cursor<B> cursor) {
    this(
      cursor,
      Objects::equals
    );
  }
  Inner(final Cursor<B> cursor, final Pred.Bi<? super A, ? super B> pred) {this.cursor = cursor;
    this.pred = pred;
  }

  @Override
  public Func.Uni<A[], Cursor<Pair<A, B>>> onArray() {
    return ts -> cursor.to(new Nested<>(ts, pred));
  }

  @Override
  public Func.Uni<Literator<A>, Cursor<Pair<A, B>>> onLiterator() {
    return null;
  }

  @Override
  public Func.Uni<Iterator<A>, Cursor<Pair<A, B>>> onIterator() {
    return null;
  }
}
