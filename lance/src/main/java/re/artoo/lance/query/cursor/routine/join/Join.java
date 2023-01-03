package re.artoo.lance.query.cursor.routine.join;

import re.artoo.lance.func.TryPredicate2;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.routine.Routine;
import re.artoo.lance.tuple.Pair;

public sealed interface Join<A, B> extends Routine<A, B> permits Inner, Nested {

  static <A, B> Join<A, Cursor<Pair<A, B>>> natural(Cursor<B> cursor) { return new Inner<>(cursor); }

  static <T, R> Join<T, Cursor<Pair<T, R>>> inner(Cursor<R> cursor, TryPredicate2<? super T, ? super R> pred) { return new Inner<>(cursor, pred); }

}
