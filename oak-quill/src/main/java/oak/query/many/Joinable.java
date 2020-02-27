package oak.query.many;

import oak.func.$2.Pred;
import oak.query.Queryable;
import oak.query.many.$2.Many;
import oak.union.$2.Union;

import java.util.ArrayList;

public interface Joinable<O> extends Queryable<O> {
  default <I> Joining<O, I> join(final Queryable<I> second) {
    return where -> {
      final var array = new ArrayList<Union<O, I>>();
      for (final var o : this) {
        for (final var i : second) {
          if (where.test(o, i))
            array.add(Union.of(o, i));
        }
      }
      return Many.from(array);
    };
  }

  @SuppressWarnings("unchecked")
  default <I> Joining<O, I> join(final I... values) {
    return join(oak.query.Many.from(values));
  }

  interface Joining<O, I> {
    Many<O, I> on(final Pred<? super O, ? super I> expression);
  }
}
