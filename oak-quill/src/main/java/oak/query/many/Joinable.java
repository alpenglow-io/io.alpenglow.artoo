package oak.query.many;

import oak.func.$2.Pre;
import oak.query.Queryable;
import oak.query.many.Joinable.Joining;
import oak.query.many.$2.Many;
import oak.union.$2.Union;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

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
    Many<O, I> on(final Pre<? super O, ? super I> expression);
  }
}
