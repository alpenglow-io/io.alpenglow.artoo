package io.artoo.lance.literator.cursor.routine.join;

import io.artoo.lance.func.Pred;
import io.artoo.lance.literator.Cursor;
import io.artoo.lance.literator.cursor.routine.Routine;
import io.artoo.lance.query.many.Joinable.Joined;

public sealed interface Join<T, R> extends Routine<T, R> permits Inner, Nested {

  static <T, R> Join<T, Cursor<Joined<T, R>>> natural(Cursor<R> cursor) { return new Inner<>(cursor); }

  static <T, R> Join<T, Cursor<Joined<T, R>>> inner(Cursor<R> cursor, Pred.Bi<? super T, ? super R> pred) { return new Inner<>(cursor, pred); }

}
