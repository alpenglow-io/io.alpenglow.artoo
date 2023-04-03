package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryPredicate2;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.tuple.Pair;

public /*sealed*/ interface Join<FIRST, SECOND> extends Cursor<Pair<FIRST, SECOND>>/* permits Inner, Left, Outer, Right*/ {
  Cursor<Pair<FIRST, SECOND>> on(TryPredicate2<? super FIRST, ? super SECOND> condition);
}
