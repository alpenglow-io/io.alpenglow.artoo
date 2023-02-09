package re.artoo.lance.query.many;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryPredicate2;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.Many;
import re.artoo.lance.query.cursor.routine.join.Join;
import re.artoo.lance.tuple.Pair;
import re.artoo.lance.value.Array;

@SuppressWarnings({"unused", "unchecked"})
public interface Joinable<ELEMENT> extends Queryable<ELEMENT> {
  default <ANOTHER, QUERYABLE extends Queryable<ANOTHER>> Joining<ELEMENT, ANOTHER> join(QUERYABLE queryable) {
    return new Default<>(this, queryable);
  }

  default <ANOTHER> Joining<ELEMENT, ANOTHER> join(ANOTHER... items) {
    return () -> cursor()
      .foldLeft(
        Array.<Pair<ELEMENT, ANOTHER>>empty(),
        (pairs, )
        );
  }

  interface Joining<A, B> extends Many.Pairs<A, B> {
    Many.Pairs<A, B> on(TryPredicate2<? super A, ? super B> on);
  }

  final class Default<FIRST, SECOND, QUERYABLE extends Queryable<SECOND>> implements Joining<FIRST, SECOND> {
    private final Queryable<FIRST> first;

    private final QUERYABLE second;

    Default(final Queryable<FIRST> first, final QUERYABLE second) {
      this.first = first;
      this.second = second;
    }

    @Override
    public Cursor<Pair<FIRST, SECOND>> cursor() {
      return first.cursor().to(Join.natural(second.cursor()));
    }
    @Override
    public Many.Pairs<FIRST, SECOND> on(final TryPredicate2<? super FIRST, ? super SECOND> on) {
      return () -> first.cursor().to(Join.inner(second.cursor(), on));
    }

  }
}
