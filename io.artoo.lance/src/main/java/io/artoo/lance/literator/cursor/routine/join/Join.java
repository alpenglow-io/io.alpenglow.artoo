package io.artoo.lance.literator.cursor.routine.join;

import io.artoo.lance.func.Pred;
import io.artoo.lance.literator.Cursor;
import io.artoo.lance.literator.cursor.routine.Routine;
import io.artoo.lance.tuple.Pair;
import io.artoo.lance.tuple.Tuple;

public sealed interface Join<A, B> extends Routine<A, B> permits Inner, Nested {

  static <A, B> Join<A, Cursor<Pair<A, B>>> natural(Cursor<B> cursor) { return new Inner<>(cursor); }

  static <T, R> Join<T, Cursor<Pair<T, R>>> inner(Cursor<R> cursor, Pred.Bi<? super T, ? super R> pred) { return new Inner<>(cursor, pred); }

}
